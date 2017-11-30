package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class CancelGuessEvent extends GameEventData {
    public CancelGuessEvent() {
        super(PlayingEventType.CANCEL_GUESS);
    }
}
