package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class OpenMultipleCellEvent extends BaseEventData {
    private final char character;

    public OpenMultipleCellEvent(char ch) {
        super(0);
        this.character = ch;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.OPEN_MULTIPLE_CELL;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String getName() {
        return null;
    }
}
