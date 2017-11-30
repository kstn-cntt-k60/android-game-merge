package kstn.game.logic.playing_event.answer;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class AnswerEvent extends GameEventData {
    private char character;

    public AnswerEvent(char character) {
        super(PlayingEventType.ANSWER);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }
}
