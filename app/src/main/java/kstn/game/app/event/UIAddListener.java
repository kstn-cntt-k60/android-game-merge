package kstn.game.app.event;

import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventType;

public class UIAddListener extends LLBaseEventData {
    private final EventType type;
    private final EventListener listener;

    public UIAddListener(EventType type, EventListener listener) {
        super(LLBaseEventType.UI_ADD_LISTENER);
        this.type = type;
        this.listener = listener;
    }

    EventType getType() {
        return type;
    }

    EventListener getListener() {
        return listener;
    }
}
