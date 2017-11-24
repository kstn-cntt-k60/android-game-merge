package kstn.game.logic.network;

import kstn.game.logic.event.EventManager;

public class NetworkForwarder {
    private final EventManager eventManager;
    private final ServerFactory serverFactory;
    private final ClientFactory clientFactory;
    private final int port = 2017;

    public NetworkForwarder(EventManager eventManager,
                            ServerFactory serverFactory,
                            ClientFactory clientFactory) {
        this.eventManager = eventManager;
        this.serverFactory = serverFactory;
        this.clientFactory = clientFactory;
    }

}
