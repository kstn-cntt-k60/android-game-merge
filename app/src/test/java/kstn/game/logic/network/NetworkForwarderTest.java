package kstn.game.logic.network;

import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.cone.ConeAccelerateEventData;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeMoveEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class NetworkForwarderTest {
    private final EventManager eventManager = new BaseEventManager();
    private final ServerFactory serverFactory = mock(ServerFactory.class);
    private final ClientFactory clientFactory = mock(ClientFactory.class);
    private final Server server = mock(Server.class);
    private final Endpoint endpoint = mock(Endpoint.class);
    private final NetworkForwarder forwarder;

    private int ip = 0x12345678;

    public NetworkForwarderTest() {
        forwarder = new NetworkForwarder(eventManager, serverFactory, clientFactory);
    }

    @Test
    public void listenShouldSetListenerWhenNotThrow() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenReturn(server);
        when(server.getEndpoint()).thenReturn(endpoint);
        forwarder.listen();
        Server.OnAcceptErrorListener listener = mock(Server.OnAcceptErrorListener.class);
        forwarder.setOnAcceptErrorListener(listener);
        verify(server).setAcceptErrorListener(listener);
        verify(endpoint).setReceiveDataListener(forwarder);
    }

    @Test
    public void listenShouldNotSetListenerWhenThrow() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenThrow(new IOException());
        try {
            forwarder.listen();
        }
        catch (IOException e) {
        }
        Server.OnAcceptErrorListener listener = mock(Server.OnAcceptErrorListener.class);
        forwarder.setOnAcceptErrorListener(listener);
        verify(server, times(0)).setAcceptErrorListener(listener);
        verify(server, times(0)).getEndpoint();
    }

    @Test
    public void forwardSendingConeEvents() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenReturn(server);
        when(server.getEndpoint()).thenReturn(endpoint);
        forwarder.listen();
        ConeMoveEventData moveEvent = new ConeMoveEventData(23);
        eventManager.trigger(moveEvent);
        verify(endpoint, times(1)).send(moveEvent);
        clearInvocations(endpoint);

        ConeAccelerateEventData accelEvent = new ConeAccelerateEventData(11, 33);
        eventManager.trigger(accelEvent);
        verify(endpoint, times(1)).send(accelEvent);
        clearInvocations(endpoint);

        forwarder.shutdown();

        eventManager.trigger(moveEvent);
        verify(endpoint, times(0)).send(moveEvent);
    }

    @Test
    public void forwardOnReceivingConeEvents() throws IOException {
        when(clientFactory.connect(ip, 2017, forwarder.parserMap)).thenReturn(endpoint);
        forwarder.connect(ip);

        EventListener listener = mock(EventListener.class);
        eventManager.addListener(ConeEventType.MOVE, listener);

        ConeMoveEventData moveEvent = new ConeMoveEventData(23);
        forwarder.onReceiveData(moveEvent);
        verify(listener, times(1)).onEvent(moveEvent);
        clearInvocations(listener);
        forwarder.onReceiveData(moveEvent);

        verify(listener, times(1)).onEvent(moveEvent);
        verify(endpoint, times(0)).send(moveEvent);

        clearInvocations(endpoint);
        clearInvocations(listener);

        forwarder.shutdown();

        forwarder.onReceiveData(moveEvent);
        verify(listener, times(1)).onEvent(moveEvent);
        verify(endpoint, times(0)).send(moveEvent);
    }

    @Test
    public void notForwardSendingConeEventsWhenThrow() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenThrow(new IOException());
        when(server.getEndpoint()).thenReturn(endpoint);
        try {
            forwarder.listen();
        }
        catch (IOException e) {
        }

        ConeMoveEventData moveEvent = new ConeMoveEventData(23);
        eventManager.trigger(moveEvent);
        verify(endpoint, times(0)).send(moveEvent);
        clearInvocations(endpoint);

        ConeAccelerateEventData accelEvent = new ConeAccelerateEventData(11, 33);
        eventManager.trigger(accelEvent);
        verify(endpoint, times(0)).send(accelEvent);
        clearInvocations(endpoint);

        forwarder.shutdown();

        eventManager.trigger(moveEvent);
        verify(endpoint, times(0)).send(moveEvent);
    }

    @Test
    public void checkParserMap() {
        Map<EventType, EventData.Parser> map = forwarder.parserMap;
        assertTrue(map.containsKey(ConeEventType.MOVE));
        assertTrue(map.containsKey(ConeEventType.ACCELERATE));
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowDoubleCallOnListen() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenReturn(server);
        when(server.getEndpoint()).thenReturn(endpoint);
        forwarder.listen();
        forwarder.listen();
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowListenAndThenConnect() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenReturn(server);
        when(server.getEndpoint()).thenReturn(endpoint);
        forwarder.listen();
        forwarder.connect(0x2344);
    }

    @Test(expected = RuntimeException.class)
    public void shouldNotAllowConnectAndThenListen() throws IOException {
        when(serverFactory.create(2017, forwarder.parserMap)).thenReturn(server);
        when(server.getEndpoint()).thenReturn(endpoint);
        when(clientFactory.connect(ip, 2017, forwarder.parserMap)).thenReturn(endpoint);
        forwarder.connect(ip);
        forwarder.listen();
    }
}
