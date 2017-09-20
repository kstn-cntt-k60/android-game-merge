package kstn.game.app.event;

public class LLBaseEventData implements LLEventData {
    private final LLEventType eventType;

    public LLBaseEventData(LLEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public LLEventType getEventType() {
        return eventType;
    }
}
