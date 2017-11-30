package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class RequestGuessEvent extends GameEventData {
    public RequestGuessEvent() {
        super(PlayingEventType.REQUEST_GUESS);
    }
}
