package kstn.game.logic.state;

/**
 * Created by qi on 09/11/2017.
 */

public class LogicStateManager {
    LogicGameState currentState;

    //

    public LogicStateManager() {
        // TODO initialize all game states
        currentState.entry();
    }

    public void makeTransitionTo(LogicGameState other) {
        currentState.exit();
        currentState = other;
        currentState.entry();
    }
}
