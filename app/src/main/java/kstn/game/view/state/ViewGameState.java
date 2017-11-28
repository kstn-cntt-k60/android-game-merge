package kstn.game.view.state;

public abstract class ViewGameState {
    final protected ViewStateManager stateManager;
    private volatile boolean viewIsReady = false;

    public ViewGameState(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public boolean isReady() {
        return viewIsReady;
    }

    protected void postEntry() {
        viewIsReady = true;
    }

    protected void preExit() {
        viewIsReady = false;
    }

    public abstract void entry();

    public abstract boolean onBack();

    public abstract void exit();
}
