package kstn.game.logic.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state.multiplayer.ministate.State;

public class MultiPlayerManager implements IEntryExit {
    private final EventManager eventManager;
    private final List<ScorePlayer> scorePlayerList = new ArrayList<>();
    private final ScorePlayerManager scoreManager;

    private State currentState;

    public MultiPlayerManager(EventManager eventManager,
                              ScorePlayerManager scoreManager) {
        this.eventManager = eventManager;
        this.scoreManager = scoreManager;
    }

    @Override
    public void entry() {
        scoreManager.entry();
    }

    void onViewReady() {
        scoreManager.onViewReady();
    }

    @Override
    public void exit() {
        scoreManager.exit();
    }
}
