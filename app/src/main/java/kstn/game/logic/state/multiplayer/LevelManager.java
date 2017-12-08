package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.IEntryExit;

public class LevelManager implements IEntryExit {
    private final EventManager eventManager;
    int level;

    private final EventListener nextQuestionListener;

    public LevelManager(EventManager eventManager) {
        this.eventManager = eventManager;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                level += 1;
            }
        };
    }

    @Override
    public void entry() {
        level = 0;
        eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public int getLevel() {
        return level;
    }
}
