package kstn.game.logic.state_event;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.GameEventData;

public abstract class StateEventData extends GameEventData {

    public StateEventData(EventType eventType) {
        super(eventType);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof EventData) {
            EventData eventData = (EventData) object;
            return eventData.getEventType() == this.getEventType();
        }
        return false;
    }
}
