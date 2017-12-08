package kstn.game.logic.state.multiplayer;

import android.util.Log;

import kstn.game.logic.cone.ConeAccelerateEventData;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.answer.AnswerEvent;
import kstn.game.logic.playing_event.cell.CellChosenEvent;
import kstn.game.logic.playing_event.guess.GuessResultEvent;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerReadyEvent;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state.multiplayer.ministate.State;

public class MultiPlayerManager implements IEntryExit {
    private final EventManager eventManager;
    private final ScorePlayerManager scoreManager;
    private final QuestionManager questionManager;
    private final CellManager cellManager;
    private final LevelManager levelManager;
    private final WifiInfo wifiInfo;

    State currentState;
    private State waitOtherPlayersState; // client default start state
    private State rotatableState; // host default start state

    public void setWaitOtherPlayersState(State state) {
        waitOtherPlayersState = state;
    }

    public void setRotatableState(State state) {
        rotatableState = state;
    }

    public void makeTransitionTo(State otherState) {
        currentState.exit();
        currentState = otherState;
        currentState.entry();
    }

    private EventListener coneAccelListener;
    private EventListener coneStopListener;
    private EventListener chooseCellListener;
    private EventListener answerListener;
    private EventListener requestGuessListener;
    private EventListener guessResultListener;
    private EventListener cancelGuessListener;
    private EventListener nextPlayerListener;

    private EventListener playerReadyListener;

    boolean viewIsReady;

    public MultiPlayerManager(EventManager eventManager,
                              final ScorePlayerManager scoreManager,
                              final QuestionManager questionManager,
                              CellManager cellManager,
                              LevelManager levelManager,
                              WifiInfo wifiInfo) {
        this.eventManager = eventManager;
        this.scoreManager = scoreManager;
        this.questionManager = questionManager;
        this.cellManager = cellManager;
        this.levelManager = levelManager;
        this.wifiInfo = wifiInfo;

        coneAccelListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ConeAccelerateEventData eventData = (ConeAccelerateEventData) event;
                currentState.coneAccel(eventData.getAngle(), eventData.getSpeedStart());
            }
        };

        coneStopListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ConeStopEventData event1 = (ConeStopEventData) event;
                currentState.coneStop(event1.getResult());
            }
        };

        chooseCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                CellChosenEvent event1 = (CellChosenEvent) event;
                currentState.chooseCell(event1.getIndex());
            }
        };

        answerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                AnswerEvent event1 = (AnswerEvent) event;
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
                GuessResultEvent event1 = (GuessResultEvent) event;
                currentState.guessResult(event1.getResult());
            }
        };

        cancelGuessListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                currentState.cancelGuess();
            }
        };

        nextPlayerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                NextPlayerEvent event1 = (NextPlayerEvent) event;
                currentState.nextPlayer(event1.getPlayerIndex());
            }
        };

        playerReadyListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerReadyEvent event1 = (PlayerReadyEvent) event;
                Log.i("ReadyListener", "Okay " + event1.getIpAddress());
                scoreManager.playerReady(event1.getIpAddress());
                if (viewIsReady && scoreManager.areAllPlayersReady()) {
                    questionManager.nextQuestion();
                    makeTransitionTo(rotatableState);
                }
            }
        };
    }

    @Override
    public void entry() {
        viewIsReady = false;
        scoreManager.loadInfoFromThisRoom();
        scoreManager.entry();
        questionManager.entry();
        cellManager.entry();
        levelManager.entry();

        eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);
        eventManager.addListener(ConeEventType.STOP, coneStopListener);
        eventManager.addListener(PlayingEventType.CELL_CHOSEN, chooseCellListener);
        eventManager.addListener(PlayingEventType.ANSWER, answerListener);
        eventManager.addListener(PlayingEventType.REQUEST_GUESS, requestGuessListener);
        eventManager.addListener(PlayingEventType.GUESS_RESULT, guessResultListener);
        eventManager.addListener(PlayingEventType.CANCEL_GUESS, cancelGuessListener);
        eventManager.addListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);

        currentState = waitOtherPlayersState;
        currentState.entry();

        if (scoreManager.thisPlayerIsHost()) {
            eventManager.addListener(PlayingEventType.PLAYER_READY, playerReadyListener);
        }
    }

    void onViewReady() {
        scoreManager.onViewReady();

        if (!scoreManager.thisPlayerIsHost()) {
            eventManager.trigger(new PlayerReadyEvent(wifiInfo.getIP()));
        }

        if (scoreManager.thisPlayerIsHost() && scoreManager.areAllPlayersReady()) {
            questionManager.nextQuestion();
            currentState = rotatableState;
        }
        viewIsReady = true;
    }

    @Override
    public void exit() {
        if (scoreManager.thisPlayerIsHost()) {
            eventManager.removeListener(PlayingEventType.PLAYER_READY, playerReadyListener);
        }

        eventManager.removeListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.removeListener(PlayingEventType.CANCEL_GUESS, cancelGuessListener);
        eventManager.removeListener(PlayingEventType.GUESS_RESULT, guessResultListener);
        eventManager.removeListener(PlayingEventType.REQUEST_GUESS, requestGuessListener);
        eventManager.removeListener(PlayingEventType.ANSWER, answerListener);
        eventManager.removeListener(PlayingEventType.CELL_CHOSEN, chooseCellListener);
        eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);

        levelManager.exit();
        cellManager.exit();
        questionManager.exit();
        scoreManager.exit();
    }
}
