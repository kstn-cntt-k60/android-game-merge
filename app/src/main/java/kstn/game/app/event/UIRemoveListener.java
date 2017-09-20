package kstn.game.app.event;

import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventType;

public class UIRemoveListener extends LLBaseEventData {
    private final EventType type;
    private final EventListener listener;

    public UIRemoveListener(EventType type, EventListener listener) {
        super(LLBaseEventType.UI_REMOVE_LISTENER);
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
