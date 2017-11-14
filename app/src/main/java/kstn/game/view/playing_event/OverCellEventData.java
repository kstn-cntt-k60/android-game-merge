package kstn.game.view.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 14/11/2017.
 */

public class OverCellEventData extends BaseEventData {
    public OverCellEventData() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.OVER_CELL;
    }

    @Override
    public String getName() {
        return null;
    }
}
