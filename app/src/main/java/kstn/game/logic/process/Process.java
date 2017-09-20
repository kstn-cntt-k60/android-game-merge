package kstn.game.logic.process;

public abstract class Process {
	
	public enum State {
		UNINITIALIZED, // Create but not running
		REMOVED, // removed but not destroy.
			 	 // can happen when it is parent of another process
		RUNNING, // living process, initialized and running
		PAUSED, // living process, initialized but paused
		SUCCEEDED, // dead process, complete successfully
		FAILED, // dead process, failed to complete
		ABORTED // dead process, aborted, may not have been started 
	}
	
	private State state = State.UNINITIALIZED;
	private Process child = null;
	
	public Process() {}
	
	public void onInit() { state = State.RUNNING; }
	public abstract void onUpdate(long deltaMs);
	public abstract void onSuccess();
	public abstract void onFail();
	public abstract void onAbort();
	
	// For ending process
	public void succeed() { state = State.SUCCEEDED; }
	public void fail() { state = State.FAILED; }
	public void abort() { state = State.ABORTED; }
	
	// Pause
	public void pause() { state = State.PAUSED; }
	public void resume() { state = State.RUNNING; }
	
	public State getState() { return state; }

	public boolean isAlive() { return state == State.RUNNING || state == State.PAUSED; }

	public boolean isDead() { 
		return state == State.SUCCEEDED || state == State.FAILED || 
				state == State.ABORTED; 
	}
	
	public boolean isRemoved() { return state == State.REMOVED; }
	
	public boolean isPaused() { return state == State.PAUSED; }
	
	public void attachChild(Process child) {
		this.child = child;
	}
	
	public Process removeChild() {
		Process child = this.child;
		this.child = null;
		return child;
	}
	
	public Process getChild() {
		return child;
	}
	
	protected void setState(State state) {
		this.state = state;
	}

}
