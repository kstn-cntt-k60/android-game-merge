package kstn.game.logic.playing_event.guess;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class GuessResultEvent extends BaseEventData {
    private final String result;

    public GuessResultEvent(String result) {
        super(0);
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.GUESS_RESULT;
    }

    @Override
    public String getName() {
        return null;
    }
}
