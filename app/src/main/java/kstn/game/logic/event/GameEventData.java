package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class GameEventData implements EventData {
    private final EventType eventType;

    public GameEventData(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {}

    public static abstract class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException { return null; }
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public long getTimeStamp() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
