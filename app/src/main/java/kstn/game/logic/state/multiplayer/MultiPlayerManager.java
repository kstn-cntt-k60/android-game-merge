package kstn.game.logic.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state.multiplayer.ministate.State;

public class MultiPlayerManager implements IEntryExit {
    private ThisRoom thisRoom;
    private List<ScorePlayer> scorePlayerList = new ArrayList<>();

    private State currentState;

    MultiPlayerManager() {

    }

    @Override
    public void entry() {
    }

    public void onViewReady() {
        for (Player player: thisRoom.getPlayerList()) {
            scorePlayerList.add(new ScorePlayer(player));
        }
        scorePlayerList.get(0).ready();
    }

    public boolean allPlayersAreReady() {
        for (ScorePlayer scorePlayer: scorePlayerList)
            if (!scorePlayer.isReady())
                return false;
        return true;
    }

    @Override
    public void exit() {

    }
}
