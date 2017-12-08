package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.guess.AcceptRequestGuessEvent;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;

public class RotatableState extends State {
    private final EventManager eventManager;
    private final MultiPlayerManager multiPlayerManager;
    private final Cone cone;

    private State rotatingState;
    private State waitGuessResultState;

    public void setRotatingState(State state) { this.rotatingState = state; }

    public void setWaitGuessResultState(State state) {
        this.waitGuessResultState = state;
    }

    public RotatableState(
                EventManager eventManager,
                MultiPlayerManager multiPlayerManager,
                Cone cone) {
        this.eventManager = eventManager;
        this.multiPlayerManager = multiPlayerManager;
        this.cone = cone;
    }

    @Override
    public void entry() {
        cone.enable();
    }

    @Override
    public void exit() {
        cone.disable();
    }

    @Override
    public void coneAccel(float angle, float speed) {
        multiPlayerManager.makeTransitionTo(rotatingState);
    }

    @Override
    public void requestGuess() {
        eventManager.trigger(new AcceptRequestGuessEvent());
        multiPlayerManager.makeTransitionTo(waitGuessResultState);
    }
}
