package kstn.game.view.state;

/**
 * Created by qi on 09/11/2017.
 */

public abstract class ViewGameState {
    final protected ViewStateManager stateManager;

    public ViewGameState(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public abstract void entry();

    public abstract void exit();
}
