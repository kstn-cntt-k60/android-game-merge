package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class OpenMultipleCellEvent extends GameEventData {
    private final char character;

    public OpenMultipleCellEvent(char ch) {
        super(PlayingEventType.OPEN_MULTIPLE_CELL);
        this.character = ch;
    }

    public char getCharacter() {
        return character;
    }
}
