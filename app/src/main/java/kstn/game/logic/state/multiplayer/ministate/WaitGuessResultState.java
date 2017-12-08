package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.ShowToastEvent;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.QuestionManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

public class WaitGuessResultState extends State {
    private final EventManager eventManager;
    private final QuestionManager questionManager;
    private final ScorePlayerManager scorePlayerManager;
    private final MultiPlayerManager multiPlayerManager;

    private State waitOtherPlayersState;
    private State rotatableState;

    public void setWaitOtherPlayersState(State state) {
        waitOtherPlayersState = state;
    }

    public void setRotatableState(State state) {
        rotatableState = state;
    }

    public WaitGuessResultState(EventManager eventManager,
                         QuestionManager questionManager,
                         ScorePlayerManager scorePlayerManager,
                         MultiPlayerManager multiPlayerManager) {
        this.eventManager = eventManager;
        this.questionManager = questionManager;
        this.scorePlayerManager = scorePlayerManager;
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void guessResult(String result) {
        eventManager.trigger(new ShowToastEvent("Đã đoán: " + result));
        if (questionManager.sameAsAnswer(result)) {
            eventManager.trigger(new ShowToastEvent("Đã đoán đúng"));
            multiPlayerManager.makeTransitionTo(rotatableState);
            questionManager.nextQuestion();
        }
        else {
            eventManager.trigger(new ShowToastEvent("Đã đoán sai"));
            scorePlayerManager.deactivateCurrentPlayer();
            multiPlayerManager.makeTransitionTo(waitOtherPlayersState);
            scorePlayerManager.nextPlayer();
        }
    }

    @Override
    public void cancelGuess() {
        multiPlayerManager.makeTransitionTo(rotatableState);
    }
}
