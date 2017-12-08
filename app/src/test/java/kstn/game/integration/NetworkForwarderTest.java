package kstn.game.integration;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.network.BaseClientFactory;
import kstn.game.app.network.BaseServerFactory;
import kstn.game.app.network.NetworkUtil;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.ServerFactory;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class NetworkForwarderTest{
    private final LLBaseEventManager llEventManager = new LLBaseEventManager();
    private final EventManager eventManager = new BaseEventManager();
    private final ServerFactory serverFactory = new BaseServerFactory(llEventManager);
    private final ClientFactory clientFactory = new BaseClientFactory(llEventManager);
    private final NetworkForwarder forwarder;

    private int ip = NetworkUtil.ipStringToInt("192.168.43.42");

    public NetworkForwarderTest() {
        forwarder = new NetworkForwarder(eventManager, serverFactory, clientFactory);
    }

    @Test
    public void sendEvent() throws IOException, InterruptedException {
        forwarder.connect(ip);
        EventListener listener = mock(EventListener.class);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, listener);
        eventManager.trigger(new RequestJoinRoomEvent(
                233, "Tung", 0));
        Thread.sleep(200);
        llEventManager.update();
        forwarder.shutdown();
    }
}
