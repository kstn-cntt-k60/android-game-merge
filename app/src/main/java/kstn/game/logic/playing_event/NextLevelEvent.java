package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

/**
 * Created by thang on 12/2/2017.
 */

public class NextLevelEvent extends GameEventData {
    public NextLevelEvent() {
        super(PlayingEventType.NEXT_LEVEL);
    }
}
