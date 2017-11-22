package kstn.game.logic.playing_event.sync;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class LogicPlayingReadyEvent extends BaseEventData {
    private final boolean sawViewReady;

    public LogicPlayingReadyEvent(boolean sawViewReady) {
        super(0);
        this.sawViewReady = sawViewReady;
    }

    public boolean sawViewReady() {
        return sawViewReady;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.LOGIC_SINGLE_PLAYER_READY;
    }

    @Override
    public String getName() {
        return null;
    }
}
