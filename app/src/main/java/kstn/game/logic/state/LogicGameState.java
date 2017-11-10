package kstn.game.logic.state;

/**
 * Created by qi on 09/11/2017.
 */

public abstract class LogicGameState {
    final protected LogicStateManager stateManager;

    public LogicGameState(LogicStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public abstract void entry();

    public abstract void exit();
}
