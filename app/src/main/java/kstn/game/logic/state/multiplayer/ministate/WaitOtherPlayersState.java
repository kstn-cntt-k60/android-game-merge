package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

public class WaitOtherPlayersState extends State {
    private State rotatableState;
    private final ScorePlayerManager scorePlayerManager;
    private final MultiPlayerManager multiPlayerManager;
    private final Cone cone;

    public void setRotatableState(State state) {
        this.rotatableState = state;
    }

    public WaitOtherPlayersState(ScorePlayerManager scorePlayerManager,
                                 MultiPlayerManager multiPlayerManager,
                                 Cone cone) {
        this.scorePlayerManager = scorePlayerManager;
        this.multiPlayerManager = multiPlayerManager;
        this.cone = cone;
    }

    @Override
    public void entry() {
        cone.disable();
    }

    @Override
    public void nextPlayer(int playerIndex) {
        if (scorePlayerManager.currentIsThisPlayer())
            multiPlayerManager.makeTransitionTo(rotatableState);
    }
}
