package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class AcceptRequestGuessEvent extends BaseEventData {
    public AcceptRequestGuessEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.ACCEPT_REQUEST_GUESS;
    }

    @Override
    public String getName() {
        return null;
    }
}
