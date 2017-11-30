package kstn.game.logic.state.multiplayer;

import java.util.HashSet;
import java.util.Set;

import kstn.game.logic.network.Connection;

public class ActiveConnections {
    Set<Connection> connections = new HashSet<>();

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public boolean isActive(Connection connection) {
        return connections.contains(connection);
    }

    public void clear() {
        connections.clear();
    }

    public int size() {
        return connections.size();
    }
}
