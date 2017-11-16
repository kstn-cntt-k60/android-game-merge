package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/11/2017.
 */

public class ViewSinglePlayerReadyEvent extends BaseEventData {

    public ViewSinglePlayerReadyEvent() {
        super(0);
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
