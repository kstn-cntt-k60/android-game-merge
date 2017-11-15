package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class NextLevelEvent extends BaseEventData {
    public NextLevelEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.NEXT_LEVEL;
    }

    @Override
    public String getName() {
        return null;
    }
}
