package kstn.game.logic.playing_event.cell;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class GiveChooseCellEvent extends GameEventData {
    public GiveChooseCellEvent() {
        super(PlayingEventType.GIVE_CHOOSE_CELL);
    }
}
