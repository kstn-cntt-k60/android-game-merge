package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class OpenCellEvent extends GameEventData {
    private final int index;

    public OpenCellEvent(int index) {
        super(PlayingEventType.OPEN_CELL);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
