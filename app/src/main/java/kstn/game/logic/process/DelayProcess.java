package kstn.game.logic.process;

public class DelayProcess extends Process {
	private long delayMs;
	private long elapsedTime = 0;
	
	public DelayProcess(Process run, long delayMs) {
		super();
		this.delayMs = delayMs;
		this.attachChild(run);
	}

	@Override
	public void onUpdate(long deltaMs) {
		elapsedTime += deltaMs;
		if (elapsedTime >= delayMs)
			this.succeed();
	}

	@Override
	public void onSuccess() {}

	@Override
	public void onFail() {}

	@Override
	public void onAbort() {}

}
