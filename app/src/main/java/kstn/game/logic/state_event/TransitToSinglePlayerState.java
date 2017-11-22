package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 13/11/2017.
 */

public class TransitToSinglePlayerState extends BaseEventData {
    public TransitToSinglePlayerState() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return StateEventType.SINGLE_PLAYER;
    }

    @Override
    public String getName() {
        return null;
    }
}
