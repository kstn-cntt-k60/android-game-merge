package kstn.game.app.network.events;

import kstn.game.app.event.LLBaseEventType;
import kstn.game.view.network.Connection;
import kstn.game.app.event.LLBaseEventData;

// Always connection != null: Can happen while server and client connected
public class TCPConnectionError extends LLBaseEventData {
    private final Connection connection;

    public TCPConnectionError(Connection connection) {
        super(LLBaseEventType.TCP_CONNECTION_ERROR);
        assert (connection != null);
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
