package kstn.game.view.state;

/**
 * Created by qi on 09/11/2017.
 */

public class ViewStateManager {
    ViewGameState currentState;

    //

    public ViewStateManager() {
        // TODO initialize all game states

        currentState.entry();
    }

    public void makeTransitionTo(ViewGameState other) {
        currentState.exit();
        currentState = other;
        currentState.entry();
    }
}
