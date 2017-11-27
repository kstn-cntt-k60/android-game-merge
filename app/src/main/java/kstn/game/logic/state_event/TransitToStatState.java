package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class TransitToStatState extends BaseEventData {

    public TransitToStatState() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return StateEventType.STAT;
    }

    @Override
    public String getName() {
        return null;
    }
}
