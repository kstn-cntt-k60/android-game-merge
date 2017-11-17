package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class ConeResultEvent extends BaseEventData {
    private int result;

    public ConeResultEvent(int result) {
        super(0);
        this.result = result;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.ROTATE_RESULT;
    }

    public int getResult() {
        return result;
    }

    @Override
    public String getName() {
        return null;
    }
}
