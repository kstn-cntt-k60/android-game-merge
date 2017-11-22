package kstn.game.logic.playing_event.sync;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayingReadyEvent extends BaseEventData {
    public PlayingReadyEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.PLAYING_READY;
    }

    @Override
    public String getName() {
        return null;
    }
}
