package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class NextLevelEvent extends GameEventData {
    public NextLevelEvent() {
        super(PlayingEventType.NEXT_LEVEL);
    }
}
