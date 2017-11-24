package kstn.game.logic.network;

import java.io.IOException;

public interface IUDPForwarder {
    void listen() throws IOException;

    void shutdown();
}
