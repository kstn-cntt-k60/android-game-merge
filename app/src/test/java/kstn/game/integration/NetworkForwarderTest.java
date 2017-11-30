package kstn.game.integration;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.network.BaseClientFactory;
import kstn.game.app.network.BaseServerFactory;
import kstn.game.app.network.NetworkUtil;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.ServerFactory;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;

public class NetworkForwarderTest{
    private final LLEventManager llEventManager = new LLBaseEventManager();
    private final EventManager eventManager = new BaseEventManager();
    private final ServerFactory serverFactory = new BaseServerFactory(llEventManager);
    private final ClientFactory clientFactory = new BaseClientFactory(llEventManager);
    private final NetworkForwarder forwarder;

    private int ip = NetworkUtil.ipStringToInt("192.168.1.160");

    public NetworkForwarderTest() {
        forwarder = new NetworkForwarder(eventManager, serverFactory, clientFactory);
    }

    @Test
    public void sendEvent() throws IOException {
        forwarder.connect(ip);
        eventManager.trigger(new RequestJoinRoomEvent(
                233, "Tung", 2));
        forwarder.shutdown();
    }
}
