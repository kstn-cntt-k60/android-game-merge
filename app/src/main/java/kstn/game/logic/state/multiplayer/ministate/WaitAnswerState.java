package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.ShowToastEvent;
import kstn.game.logic.playing_event.SongFailEvent;
import kstn.game.logic.playing_event.SongTingTingEvent;
import kstn.game.logic.state.multiplayer.CellManager;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

public class WaitAnswerState extends State {
    private final EventManager eventManager;
    private final CellManager cellManager;
    private final ScorePlayerManager scorePlayerManager;
    private final MultiPlayerManager multiPlayerManager;

    private State waitOtherPlayersState;
    private State rotatableState;
    private RotatingState rotatingState;
    private State waitGuessResultState;

    public void setWaitOtherPlayersState(State state) {
        waitOtherPlayersState = state;
    }

    public void setRotatableState(State state) {
        rotatableState = state;
    }

    public void setRotatingState(RotatingState rotatingState) {
        this.rotatingState = rotatingState;
    }

    public WaitAnswerState(
            EventManager eventManager,
            CellManager cellManager,
            ScorePlayerManager scorePlayerManager,
            MultiPlayerManager multiPlayerManager) {
        this.eventManager = eventManager;
        this.cellManager = cellManager;
        this.scorePlayerManager = scorePlayerManager;
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void answer(char ch) {
        int times = cellManager.openMultipleCells(ch);
        if (times == 0) {
            eventManager.trigger(new ShowToastEvent("Bạn đã đoán sai"));
            eventManager.trigger(new SongFailEvent());

            if (scorePlayerManager.countActivePlayers() == 1) {
                multiPlayerManager.makeTransitionTo(waitGuessResultState);
            }
            else {
                scorePlayerManager.nextPlayer();
                multiPlayerManager.makeTransitionTo(waitOtherPlayersState);
            }
        }
        else {
            int preResult = rotatingState.result;
            multiPlayerManager.makeTransitionTo(rotatableState);

            int preScore = scorePlayerManager.getScore();
            if (ConeResult.isScore(preResult)) {
                scorePlayerManager.setScore(
                        preScore + times * rotatingState.rotatedScore
                );
            }
            else if (preResult == ConeResult.MUL_2) {
                scorePlayerManager.setScore(
                        preScore * 2
                );
            }
            else if (preResult == ConeResult.DIV_2) {
                scorePlayerManager.setScore(
                        (preScore + 1) / 2
                );
            }
            int diffScore = scorePlayerManager.getScore() - preScore;

            eventManager.trigger(new ShowToastEvent(Integer.toString(diffScore)));
            eventManager.trigger(new SongTingTingEvent());
        }
    }

}
