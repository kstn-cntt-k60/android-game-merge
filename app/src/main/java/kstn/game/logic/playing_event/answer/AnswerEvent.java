package kstn.game.logic.playing_event.answer;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class AnswerEvent extends BaseEventData {
    private char character;

    public AnswerEvent(char character) {
        super(0);
        this.character = character;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.ANSWER;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String getName() {
        return null;
    }
}
