package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.state.multiplayer.MultiPlayerManager;

public class RotatableState extends State {
    private State rotatingState;
    private final MultiPlayerManager multiPlayerManager;

    public void setRotatingState(State state) { this.rotatingState = state; }

    public RotatableState(MultiPlayerManager multiPlayerManager) {
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void coneAccel(float angle, float speed) {
        multiPlayerManager.makeTransitionTo(rotatingState);
    }
}
