package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

/**
 * Created by qi on 16/11/2017.
 */

public class CellChosenEvent extends BaseEventData {
    private final int index;

    public CellChosenEvent(int index) {
        super(0);
        this.index = index;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.CELL_CHOSEN;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return null;
    }
}
