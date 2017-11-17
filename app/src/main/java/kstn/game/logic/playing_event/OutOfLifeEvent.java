package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class OutOfLifeEvent extends BaseEventData {
    public OutOfLifeEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.OUT_OF_LIFE;
    }

    @Override
    public String getName() {
        return null;
    }
}
