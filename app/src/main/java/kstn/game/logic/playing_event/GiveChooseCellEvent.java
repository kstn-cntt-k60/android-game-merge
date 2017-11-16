package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/11/2017.
 */

public class GiveChooseCellEvent extends BaseEventData {
    public GiveChooseCellEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.GIVE_CHOOSE_CELL;
    }

    @Override
    public String getName() {
        return null;
    }
}
