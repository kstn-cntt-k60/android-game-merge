package kstn.game.logic.state.multiplayer.ministate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.ShowToastEvent;
import kstn.game.logic.playing_event.SongFailEvent;
import kstn.game.logic.playing_event.SongTingTingEvent;
import kstn.game.logic.playing_event.answer.GiveAnswerEvent;
import kstn.game.logic.playing_event.cell.GiveChooseCellEvent;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

public class RotatingState extends State {
    private Random random = new Random();
    private final EventManager eventManager;
    private final ScorePlayerManager scorePlayerManager;
    private final MultiPlayerManager multiPlayerManager;

    private List<Integer> coneCells = new ArrayList<>(20);

    private State waitOtherPlayersState;
    private State rotatableState;
    private State waitChooseCellState;
    private State waitAnswerState;
    private State waitGuessResultState;

    public void setWaitOtherPlayersState(State state) {
        waitOtherPlayersState = state;
    }

    public void setRotatableState(State state) {
        this.rotatableState = state;
    }

    public void setWaitChooseCellState(State state) {
        this.waitChooseCellState = state;
    }

    public void setWaitAnswerState(State state) {
        this.waitAnswerState = state;
    }

    public void setWaitGuessResultState(State state) {
        this.waitGuessResultState = state;
    }

    int result;
    int rotatedScore;

    private void initConeCells() {
        coneCells.add(ConeResult._800);
        coneCells.add(ConeResult._900);
        coneCells.add(ConeResult.BONUS);
        coneCells.add(ConeResult._300);

        coneCells.add(ConeResult._200);
        coneCells.add(ConeResult.BONUS_LIFE);
        coneCells.add(ConeResult._100);
        coneCells.add(ConeResult._500);

        coneCells.add(ConeResult.DIV_2);
        coneCells.add(ConeResult._600);
        coneCells.add(ConeResult.LOST_LIFE);
        coneCells.add(ConeResult._700);

        coneCells.add(ConeResult._300);
        coneCells.add(ConeResult.LUCKY);
        coneCells.add(ConeResult._400);
        coneCells.add(ConeResult._300);

        coneCells.add(ConeResult.MUL_2);
        coneCells.add(ConeResult._200);
        coneCells.add(ConeResult._100);
        coneCells.add(ConeResult.LOST_SCORE);
    }

    public RotatingState(EventManager eventManager,
                         ScorePlayerManager scorePlayerManager,
                         MultiPlayerManager multiPlayerManager) {
        this.eventManager = eventManager;
        this.scorePlayerManager = scorePlayerManager;
        this.multiPlayerManager = multiPlayerManager;

        initConeCells();
    }

    @Override
    public void coneStop(int coneIndex) {
        result = coneCells.get(coneIndex);
        if (ConeResult.isScore(result)) {
            rotatedScore = ConeResult.getScore(result);
            eventManager.trigger(new GiveAnswerEvent());
            multiPlayerManager.makeTransitionTo(waitAnswerState);
            return;
        }

        switch (result) {
            case ConeResult.LOST_SCORE:
                scorePlayerManager.setScore(0);
                eventManager.trigger(new ShowToastEvent("Mất điểm"));
                eventManager.trigger(new SongFailEvent());
                multiPlayerManager.makeTransitionTo(rotatableState);
                break;

            case ConeResult.LOST_LIFE:
                eventManager.trigger(new ShowToastEvent("Mất lượt"));
                eventManager.trigger(new SongFailEvent());
                if (scorePlayerManager.countActivePlayers() == 1) {
                    scorePlayerManager.deactivateCurrentPlayer();
                    multiPlayerManager.makeTransitionTo(waitGuessResultState);
                }
                else {
                    multiPlayerManager.makeTransitionTo(waitOtherPlayersState);
                    scorePlayerManager.nextPlayer();
                }
                break;

            case ConeResult.DIV_2:
            case ConeResult.MUL_2:
                eventManager.trigger(new GiveAnswerEvent());
                multiPlayerManager.makeTransitionTo(waitAnswerState);
                break;

            case ConeResult.BONUS:
                int rand = random.nextInt(9);
                scorePlayerManager.setScore(
                        scorePlayerManager.getScore() + (rand + 1) * 100);
                eventManager.trigger(new ShowToastEvent(
                        "" + scorePlayerManager.getScore()));
                eventManager.trigger(new SongTingTingEvent());
                multiPlayerManager.makeTransitionTo(rotatableState);
                break;

            case ConeResult.BONUS_LIFE:
                eventManager.trigger(new ShowToastEvent("Thêm Lượt"));
                eventManager.trigger(new SongTingTingEvent());
                multiPlayerManager.makeTransitionTo(rotatableState);
                break;

            case ConeResult.LUCKY:
                eventManager.trigger(new ShowToastEvent("May mắn"));
                eventManager.trigger(new SongTingTingEvent());
                eventManager.trigger(new GiveChooseCellEvent());
                multiPlayerManager.makeTransitionTo(waitChooseCellState);
                break;
        }
    }
}
