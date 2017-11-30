package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class AcceptRequestGuessEvent extends GameEventData {
    public AcceptRequestGuessEvent() {
        super(PlayingEventType.ACCEPT_REQUEST_GUESS);
    }
}
