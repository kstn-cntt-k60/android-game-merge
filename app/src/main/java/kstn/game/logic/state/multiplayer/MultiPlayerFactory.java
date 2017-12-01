package kstn.game.logic.state.multiplayer;

import kstn.game.logic.state.LogicStateManager;

public class MultiPlayerFactory {
    private final LogicStateManager stateManager;

    public MultiPlayerFactory(LogicStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public LogicPlayingState create() {
        return null;
    }
}
