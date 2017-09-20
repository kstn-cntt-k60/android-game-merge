package kstn.game.app.process;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import kstn.game.logic.process.Process;
import kstn.game.logic.process.Process.State;
import kstn.game.logic.process.ProcessManager;

public class BaseProcessManager implements ProcessManager {
	
	private List<Process> processList = new LinkedList<>();
	
	public BaseProcessManager() {}
	
	public long updateProcesses(long deltaMs) {
		long successCount = 0;
		long failCount = 0;

		ListIterator<Process> it = processList.listIterator();
		while (it.hasNext()) {
			Process current = it.next();
			if (current.getState() == State.UNINITIALIZED) 
				current.onInit();
			
			if (current.getState() == State.RUNNING) 
				current.onUpdate(deltaMs);
			
			if (current.isDead()) {
				switch (current.getState()) {
				case SUCCEEDED:
					current.onSuccess();
					Process child = current.removeChild();
					if (child != null) {
						it.set(child);
					}
					else {
						it.remove();
						successCount++;
					}
					break;

				case FAILED:
					it.remove();
					current.onFail();
					failCount++;
					break;

				case ABORTED:
					it.remove();
					current.onAbort();
					failCount++;
					break;
					
				default:
					break;
				}
			}
		}
		
		return successCount << 32 | failCount;
	}

	@Override
	public void attachProcess(Process process) {
		processList.add(process);
	}
	
	public void abortAllProcesses(boolean immediate) {
		if (immediate) {
			for (Process p: processList) 
				p.onAbort();
			processList.clear();
		}
		else {
			for (Process p: processList)
				p.abort();
		}
	}

	@Override
	public long getProcessCount() {
		return processList.size();
	}
}
