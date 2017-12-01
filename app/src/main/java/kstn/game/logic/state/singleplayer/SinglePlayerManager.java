package kstn.game.logic.state.singleplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeAccelerateEventData;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.data.QuestionManagerDAO;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.model.QuestionModel;
import kstn.game.logic.playing_event.ConeResultEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.ResultGameOverEvent;
import kstn.game.logic.playing_event.answer.AnswerEvent;
import kstn.game.logic.playing_event.answer.GiveAnswerEvent;
import kstn.game.logic.playing_event.cell.CellChosenEvent;
import kstn.game.logic.playing_event.cell.GiveChooseCellEvent;
import kstn.game.logic.playing_event.cell.OpenCellEvent;
import kstn.game.logic.playing_event.cell.OpenMultipleCellEvent;
import kstn.game.logic.playing_event.guess.AcceptRequestGuessEvent;
import kstn.game.logic.playing_event.guess.GuessResultEvent;
import kstn.game.logic.playing_event.player.SinglePlayerStateChangeEvent;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.logic.state_event.TransitToSingleResultState;

public class SinglePlayerManager {
    private LogicStateManager stateManager;

    private SinglePlayerModel player;

    private String answer;
    private String nonSpaceAnswer;
    private boolean[] isOpenedCells;

    private boolean isGuessed = false;
    private boolean rightGuess;

    private void openMultiCells(int ch) {
        for (int i = 0; i < isOpenedCells.length; i++) {
            if (!isOpenedCells[i] && ch == nonSpaceAnswer.charAt(i))
                isOpenedCells[i] = true;
        }
    }

    private boolean allCellsAreOpened() {
        if (isOpenedCells == null)
            return false;

        for (int i = 0; i < isOpenedCells.length; i++)
            if (!isOpenedCells[i])
                return false;
        return true;
    }

    private void setQuestion(String question, String answer) {
        this.answer = answer;
        String tmp = answer.replaceAll("\\s+", "");
        nonSpaceAnswer = tmp.toUpperCase();

        isOpenedCells = new boolean[nonSpaceAnswer.length()];
        for (int i = 0; i < isOpenedCells.length; i++)
            isOpenedCells[i] = false;

        isGuessed = false;
        rightGuess = false;
    }

    private QuestionManagerDAO questionManager;

    private EventListener playingReadyListener;

    private EventListener coneAccelListener;
    private EventListener coneStopListener;
    private EventListener answerListener;
    private EventListener cellChosenListener;
    private EventListener requestGuessListener;
    private EventListener guessResultListener;
    private EventListener cancelGuessListener;

    private List<Integer> coneCells;

    private Random random = new Random();

    private Cone cone;

    private State currentState;
    private final RotatableState rotatableState;
    private final RotatingState rotatingState;
    private final WaitAnswerState waitAnswerState;
    private final WaitChooseCellState waitChooseCellState;
    private final WaitGuessResultState waitGuessResultState;

    private void makeTransitionTo(State other) {
        currentState.exit();
        currentState = other;
        currentState.entry();
    }

    private void initConeCells() {
        coneCells = new ArrayList<>(20);
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

    private void newQuestion() {
        QuestionModel questionModel = questionManager.getRandomQuestion();
        String question = questionModel.getQuestion();
        String answer = questionModel.getAnswer();
        stateManager.eventManager.trigger(
                new NextQuestionEvent(question, answer));
        setQuestion(question, answer);
    }

    public SinglePlayerManager(
            Cone cone_,
            LogicStateManager stateManager_) {
        this.stateManager = stateManager_;
        this.cone = cone_;

        rotatableState = new RotatableState();
        rotatingState = new RotatingState();
        waitAnswerState = new WaitAnswerState();
        waitChooseCellState = new WaitChooseCellState();
        waitGuessResultState = new WaitGuessResultState();

        questionManager = new QuestionManagerDAO(stateManager.mainActivity);
        questionManager.open();

        initConeCells();

        playingReadyListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                newQuestion();
            }
        };

        coneAccelListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeAccelerateEventData event = (ConeAccelerateEventData)event_;
                currentState.coneAccel(event.getAngle(), event.getSpeedStart());
            }
        };

        coneStopListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeStopEventData event = (ConeStopEventData)event_;
                currentState.coneStop(event.getResult());
            }
        };

        cellChosenListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                CellChosenEvent event1 = (CellChosenEvent)event;
                currentState.chooseCell(event1.getIndex());
            }
        };

        answerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                AnswerEvent event1 = (AnswerEvent)event;
                currentState.answer(event1.getCharacter());
            }
        };

        requestGuessListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                currentState.requestGuess();
            }
        };

        guessResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                GuessResultEvent event1 = (GuessResultEvent)event;
                currentState.guessResult(event1.getResult());
            }
        };

        cancelGuessListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                currentState.cancelGuess();
            }
        };
    }

    public void entry() {
        player = new SinglePlayerModel();

        currentState = rotatableState;
        currentState.entry();

        stateManager.eventManager.addListener(PlayingEventType.PLAYING_READY, playingReadyListener);
        stateManager.eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.addListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.addListener(PlayingEventType.ANSWER, answerListener);
        stateManager.eventManager.addListener(PlayingEventType.CELL_CHOSEN, cellChosenListener);

        stateManager.eventManager.addListener(PlayingEventType.REQUEST_GUESS,
                requestGuessListener);
        stateManager.eventManager.addListener(PlayingEventType.GUESS_RESULT,
                guessResultListener);
        stateManager.eventManager.addListener(PlayingEventType.CANCEL_GUESS,
                cancelGuessListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.removeListener(PlayingEventType.ANSWER, answerListener);
        stateManager.eventManager.removeListener(PlayingEventType.CELL_CHOSEN, cellChosenListener);
        stateManager.eventManager.removeListener(PlayingEventType.PLAYING_READY, playingReadyListener);

        stateManager.eventManager.removeListener(PlayingEventType.REQUEST_GUESS,
                requestGuessListener);
        stateManager.eventManager.removeListener(PlayingEventType.GUESS_RESULT,
                guessResultListener);
        stateManager.eventManager.removeListener(PlayingEventType.CANCEL_GUESS,
                cancelGuessListener);
    }

    private void notifyState() {
        stateManager.eventManager.trigger(
                new SinglePlayerStateChangeEvent(player.getScore(), player.getLife()));
    }

    private abstract class State {
        void entry() {}

        void exit() {}

        void coneAccel(float startAngle, float speed) {}

        void coneStop(int value) {}

        void chooseCell(int cellIndex) {}

        void answer(char ch) {}

        void requestGuess() {}

        void guessResult(String result) {}

        void cancelGuess() {}
    }

    private class RotatableState extends State {
        @Override
        void entry() {
            boolean allOpen = allCellsAreOpened();
            if (player.getLife() != 0 && isGuessed) {
                if (rightGuess) {
                    player.setScore(player.getScore() + 1000);
                    notifyState();
                }
                else {
                    player.setLife(player.getLife() - 1);
                    notifyState();
                    if (player.getLife() == 0) {
                        int score = player.getScore();
                        Log.d("Game Over",score+"" );

                        stateManager.eventManager.queue(new TransitToSingleResultState());
                        stateManager.eventManager.queue(new ResultGameOverEvent(score));

                        return;
                    }
                }
                newQuestion();
                cone.enable();
                return;
            }

            if (!isGuessed && allOpen) {
                requestGuess();
                cone.enable();
                return;
            }

            if (player.getLife() == 0) {
                if (isGuessed) {
                    int score = player.getScore();
                    ResultGameOverEvent event = new ResultGameOverEvent(score);
                    stateManager.eventManager.queue(new TransitToSingleResultState());
                    stateManager.eventManager.queue(event);
                } else {
                    requestGuess();
                }
                cone.enable();
                return;
            }

            cone.enable();
        }

        @Override
        void exit() {
            cone.disable();
        }

        @Override
        void coneAccel(float angle, float speed) {
            makeTransitionTo(rotatingState);
        }

        @Override
        void requestGuess() {
            stateManager.eventManager.trigger(new AcceptRequestGuessEvent());
            makeTransitionTo(waitGuessResultState);
        }
    }

    private class RotatingState extends State {
        int rotatedScore;
        int result;

        @Override
        void coneStop(int index) {
            result = coneCells.get(index);
            stateManager.eventManager.trigger(new ConeResultEvent(result));

            if (ConeResult.isScore(result)) {
                rotatedScore = ConeResult.getScore(result);
                stateManager.eventManager.trigger(new GiveAnswerEvent());
                makeTransitionTo(waitAnswerState);
                return;
            }

            switch (result) {
                case ConeResult.LOST_SCORE:
                    player.setScore(0);
                    notifyState();
                    makeTransitionTo(rotatableState);
                    break;

                case ConeResult.LOST_LIFE:
                    player.setLife(player.getLife() - 1);
                    notifyState();
                    makeTransitionTo(rotatableState);
                    break;

                case ConeResult.DIV_2:
                case ConeResult.MUL_2:
                    stateManager.eventManager.trigger(new GiveAnswerEvent());
                    makeTransitionTo(waitAnswerState);
                    break;

                case ConeResult.BONUS:
                    int rand = random.nextInt(9);
                    player.setScore(player.getScore() + (rand + 1) * 100);
                    notifyState();
                    makeTransitionTo(rotatableState);
                    break;

                case ConeResult.BONUS_LIFE:
                    player.setLife(player.getLife() + 1);
                    makeTransitionTo(rotatableState);
                    notifyState();
                    break;

                case ConeResult.LUCKY:
                    stateManager.eventManager.trigger(new GiveChooseCellEvent());
                    makeTransitionTo(waitChooseCellState);
                    break;
            }
        }
    }

    private class WaitChooseCellState extends State {
        @Override
        void chooseCell(int cellIndex) {
            isOpenedCells[cellIndex] = true;
            stateManager.eventManager.trigger(new OpenCellEvent(cellIndex));
            makeTransitionTo(rotatableState);
        }
    }

    private class WaitAnswerState extends State {
        @Override
        void answer(char ch) {
            stateManager.eventManager.trigger(new OpenMultipleCellEvent(ch));
            openMultiCells(ch);
            int count = countCharOccurrences(nonSpaceAnswer, ch);
            if (count == 0) {
                player.setLife(player.getLife() - 1);
                notifyState();
            }
            else {
                int preResult =  rotatingState.result;
                if (ConeResult.isScore(preResult)) {
                    player.setScore(player.getScore() + count * rotatingState.rotatedScore);
                }
                else if (preResult == ConeResult.MUL_2) {
                    player.setScore(player.getScore() * 2);
                }
                else if (preResult == ConeResult.DIV_2) {
                    player.setScore((player.getScore() + 1)/ 2);
                }
            }
            makeTransitionTo(rotatableState);
            notifyState();
        }
    }

    private class WaitGuessResultState extends State {
        @Override
        void guessResult(String result) {
            isGuessed = true;
            if (result.toUpperCase().equals(answer.toUpperCase())) {
                rightGuess = true;
            }
            else {
                rightGuess = false;
            }
            makeTransitionTo(rotatableState);
        }

        @Override
        void cancelGuess() {
            isGuessed = false;
            makeTransitionTo(rotatableState);
        }
    }

    private int countCharOccurrences(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == ch)
                count++;
        return count;
    }
}
