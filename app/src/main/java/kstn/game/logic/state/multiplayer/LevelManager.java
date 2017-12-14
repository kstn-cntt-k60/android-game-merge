package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.MultiGameOverEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state_event.TransitToResultState;

public class LevelManager implements IEntryExit {
    private final EventManager eventManager;
    private final QuestionManager questionManager;
    private final ScorePlayerManager scorePlayerManager;
    int level;

    private final EventListener nextQuestionListener;
    private final EventListener gameOverListener;

    public LevelManager(final EventManager eventManager,
                        QuestionManager questionManager,
                        ScorePlayerManager scorePlayerManager) {
        this.eventManager = eventManager;
        this.questionManager = questionManager;
        this.scorePlayerManager = scorePlayerManager;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                level += 1;
            }
        };

        gameOverListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                eventManager.queue(new TransitToResultState());
            }
        };
    }

    @Override
    public void entry() {
        level = 0;
        eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        eventManager.addListener(PlayingEventType.MULTI_GAME_OVER, gameOverListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.MULTI_GAME_OVER, gameOverListener);
        eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    private void gameOver() {
        eventManager.trigger(new MultiGameOverEvent());
    }

    public void nextLevel() {
        if (level == 3) {
            scorePlayerManager.chooseBiggestScorePlayer();
            questionManager.nextQuestion();
        }
        else if (level == 4) {
            gameOver();
        }
        else {
            scorePlayerManager.activateAllPlayers();
            questionManager.nextQuestion();
        }
    }
}
