package kstn.game.app.event;

import kstn.game.logic.event.EventData;

public class UIQueueEvent extends LLBaseEventData {
    private final EventData event;

    public UIQueueEvent(EventData event) {
        super(LLBaseEventType.UI_QUEUE_EVENT);
        this.event = event;
    }

    public EventData getEvent() {
        return event;
    }
}
