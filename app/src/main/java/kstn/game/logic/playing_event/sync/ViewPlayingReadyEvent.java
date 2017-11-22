package kstn.game.logic.playing_event.sync;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class ViewPlayingReadyEvent extends BaseEventData {
    private final boolean sawLogicReady;

    public ViewPlayingReadyEvent(boolean sawLogicReady) {
        super(0);
        this.sawLogicReady = sawLogicReady;
    }

    public boolean sawLogicReady() {
        return sawLogicReady;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.VIEW_SINGLE_PLAYER_READY;
    }

    @Override
    public String getName() {
        return null;
    }
}
