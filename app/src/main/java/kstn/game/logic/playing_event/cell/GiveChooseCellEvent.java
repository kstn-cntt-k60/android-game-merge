package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

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
