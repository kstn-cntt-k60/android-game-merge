package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.network.Connection;

public abstract class GameEventData implements EventData {
    private final EventType eventType;
    private Connection connection;

    public GameEventData(EventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {}

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

    @Override
    public boolean equals(Object object) {
        if (object instanceof GameEventData) {
            GameEventData event = (GameEventData) object;
            return event.getEventType() == this.getEventType();
        }
        return false;
    }
}
