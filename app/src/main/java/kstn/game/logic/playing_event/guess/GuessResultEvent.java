package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class GuessResultEvent extends GameEventData {
    private final String result;

    public GuessResultEvent(String result) {
        super(PlayingEventType.GUESS_RESULT);
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
