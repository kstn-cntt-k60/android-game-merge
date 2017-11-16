package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class OpenCellEvent extends BaseEventData {
    private final int index;

    public OpenCellEvent(int index) {
        super(0);
        this.index = index;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.OPEN_CELL;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return null;
    }
}
