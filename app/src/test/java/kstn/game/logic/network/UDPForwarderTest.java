package kstn.game.logic.network;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class UDPForwarderTest {
    private EventManager eventManager = new BaseEventManager();
    private WifiInfo wifiInfo = mock(WifiInfo.class);
    private UDPManagerFactory factory = mock(UDPManagerFactory.class);
    private UDPManager manager = mock(UDPManager.class);
    private UDPForwarder forwarder;
    private int ip = 0x12345678;
    private int mask = 0xffffff00;

    public UDPForwarderTest() {
        forwarder = new UDPForwarder(eventManager, factory, wifiInfo);
        when(wifiInfo.getMask()).thenReturn(mask);
        when(wifiInfo.getIP()).thenReturn(ip);
    }

    @Test
    public void listenShouldCreateUDPManagerWhenNotThrow() throws IOException {
        when(factory.create(ip, 2017, mask, forwarder.parserMap)).thenReturn(manager);
        forwarder.listen();
        Assert.assertNotNull(forwarder.manager);
        verify(manager, times(1)).setReceiveDataListener(forwarder);
    }

    @Test
    public void shouldForwardSendingEventButNotCallItself() throws IOException {
        when(factory.create(ip, 2017, mask, forwarder.parserMap)).thenReturn(manager);
        forwarder.listen();
        EventListener listener = mock(EventListener.class);
        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, listener);
        EventData event = new SawCreatedRoomEvent(23, "TUNG", 44);
        eventManager.trigger(event);
        verify(listener, times(1)).onEvent(event);
        verify(manager, times(1)).broadcast(event);
    }

    @Test
    public void shouldForwardReceivingEventButNotCallItself() throws IOException {
        when(factory.create(ip, 2017, mask, forwarder.parserMap)).thenReturn(manager);
        forwarder.listen();
        EventListener listener = mock(EventListener.class);
        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, listener);
        SawCreatedRoomEvent event = new SawCreatedRoomEvent(22, "aa", 33);
        forwarder.onReceiveData(event);
        verify(listener, times(1)).onEvent(event);
        verify(manager, times(0)).broadcast(event);
    }

    @Test
    public void shouldNotCallBroadcastWhenManagerIsNull() throws IOException {
        when(factory.create(ip, 2017, mask, forwarder.parserMap)).thenThrow(new IOException());
        try {
            forwarder.listen();
        }
        catch (IOException e) {
        }
        eventManager.trigger(new SawCreatedRoomEvent(22, "aa", 33));
        verify(manager, times(0)).broadcast(any(EventData.class));
    }
}
