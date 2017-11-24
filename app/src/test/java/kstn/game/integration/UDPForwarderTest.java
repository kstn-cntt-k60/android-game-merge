package kstn.game.integration;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.network.NetworkUtil;
import kstn.game.app.network.UDPBaseManagerFactory;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UDPForwarderTest {
    private LLEventManager llEventManager = new LLBaseEventManager();
    private EventManager eventManager = new BaseEventManager();
    private WifiInfo wifiInfo = mock(WifiInfo.class);
    private UDPForwarder forwarder;

    public UDPForwarderTest() {
        UDPManagerFactory factory = new UDPBaseManagerFactory(llEventManager);
        forwarder = new UDPForwarder(eventManager, factory, wifiInfo);
    }

    @Test
    public void shouldSentRoomEvent() throws IOException {
        when(wifiInfo.getIP()).thenReturn(NetworkUtil.ipStringToInt("192.168.1.75"));
        when(wifiInfo.getMask()).thenReturn(NetworkUtil.ipStringToInt("255.255.255.0"));
        forwarder.listen();
        // eventManager.trigger(new SawCreatedRoomEvent(344, "ROOM", 3));
        forwarder.sendEvent(new SawCreatedRoomEvent(34, "Tung", 51));
        forwarder.sendEvent(new SawCreatedRoomEvent(132, "Linh", 10));
        forwarder.shutdown();
    }
}
