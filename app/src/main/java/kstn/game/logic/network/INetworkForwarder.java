package kstn.game.logic.network;

import java.io.IOException;

import kstn.game.logic.event.EventData;

public interface INetworkForwarder {
    void connect(int ipAddress) throws IOException;

    void send(EventData event);

    void shutdown();
}