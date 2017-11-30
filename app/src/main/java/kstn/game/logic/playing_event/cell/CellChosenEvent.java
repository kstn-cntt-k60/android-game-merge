package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class CellChosenEvent extends GameEventData {
    private final int index;

    public CellChosenEvent(int index) {
        super(PlayingEventType.CELL_CHOSEN);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
