package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.ShowToastEvent;
import kstn.game.logic.playing_event.SongFailEvent;
import kstn.game.logic.playing_event.SongTingTingEvent;
import kstn.game.logic.playing_event.guess.AcceptRequestGuessEvent;
import kstn.game.logic.state.multiplayer.CellManager;
import kstn.game.logic.state.multiplayer.LevelManager;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.QuestionManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

public class WaitGuessResultState extends State {
    private final EventManager eventManager;
    private final QuestionManager questionManager;
    private final LevelManager levelManager;
    private final ScorePlayerManager scorePlayerManager;
    private final CellManager cellManager;
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
                                LevelManager levelManager,
                                ScorePlayerManager scorePlayerManager,
                                CellManager cellManager,
                                MultiPlayerManager multiPlayerManager) {
        this.eventManager = eventManager;
        this.questionManager = questionManager;
        this.levelManager = levelManager;
        this.scorePlayerManager = scorePlayerManager;
        this.cellManager = cellManager;
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void entry() {
        eventManager.trigger(new AcceptRequestGuessEvent());
    }

    @Override
    public void guessResult(String result) {
        eventManager.trigger(new ShowToastEvent("Đã đoán: " + result));
        if (questionManager.sameAsAnswer(result)) {
            eventManager.trigger(new SongTingTingEvent());
            eventManager.trigger(new ShowToastEvent("Đã đoán đúng"));

            int preScore = scorePlayerManager.getScore();
            scorePlayerManager.setScore(preScore + 1000);
            eventManager.trigger(new ShowToastEvent("+1000"));
            multiPlayerManager.makeTransitionTo(rotatableState);
            levelManager.nextLevel();
        }
        else {
            eventManager.trigger(new SongFailEvent());
            eventManager.trigger(new ShowToastEvent("Đã đoán sai"));
            scorePlayerManager.deactivateCurrentPlayer();
            multiPlayerManager.makeTransitionTo(waitOtherPlayersState);
            if (scorePlayerManager.countActivePlayers() == 0) {
                levelManager.nextLevel();
                scorePlayerManager.revalidateCurrentPlayerIndex();
            }
            else {
                scorePlayerManager.nextPlayer();
            }
        }
    }

    @Override
    public void cancelGuess() {
        if (scorePlayerManager.countActivePlayers() == 0) {
            eventManager.trigger(new AcceptRequestGuessEvent());
        }
        else if (cellManager.allCellsAreOpened()) {
            eventManager.trigger(new AcceptRequestGuessEvent());
        }
        else {
            multiPlayerManager.makeTransitionTo(rotatableState);
        }
    }
}
