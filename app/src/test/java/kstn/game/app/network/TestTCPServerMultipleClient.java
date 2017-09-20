package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.TestEventType;
import kstn.game.view.network.ClientFactory;
import kstn.game.view.network.Connection;
import kstn.game.view.network.Endpoint;
import kstn.game.view.network.Server;
import kstn.game.view.network.ServerFactory;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestTCPServerMultipleClient {
    private final LLBaseEventManager serverEventManager = new LLBaseEventManager();
    private final LLBaseEventManager clientEventManager1 = new LLBaseEventManager();
    private final LLBaseEventManager clientEventManager2 = new LLBaseEventManager();
    private final LLBaseEventManager clientEventManager3 = new LLBaseEventManager();

    private final ServerFactory serverFactory = new BaseServerFactory(serverEventManager);
    private final ClientFactory clientFactory1 = new BaseClientFactory(clientEventManager1);
    private final ClientFactory clientFactory2 = new BaseClientFactory(clientEventManager2);
    private final ClientFactory clientFactory3 = new BaseClientFactory(clientEventManager3);

    private final String ip = "127.0.0.1";
    private final int localhost = NetworkUtil.ipStringToInt(ip);
    private final int port = 2346;
    private final Map<EventType, EventData.Parser> parserMap = new HashMap<>();

    private final TestEventData1 testEvent1 = new TestEventData1(225, 667, "TUNG");
    private final TestEventData1 testEvent2 = new TestEventData1(225, 667, "THANG");
    private final TestEventData1 testEvent3 = new TestEventData1(225, 667, "QUY");

    private final Endpoint.OnReceiveDataListener receiveDataServer
            = mock(Endpoint.OnReceiveDataListener.class);
    private final Endpoint.OnConnectionErrorListener connectionErrorServer
            = mock(Endpoint.OnConnectionErrorListener.class);
    private final Server.OnAcceptErrorListener serverAcceptError
            = mock(Server.OnAcceptErrorListener.class);

    private final Endpoint.OnReceiveDataListener receiveDataClient1
            = mock(Endpoint.OnReceiveDataListener.class);
    private final Endpoint.OnConnectionErrorListener connectionErrorClient1
            = mock(Endpoint.OnConnectionErrorListener.class);

    private final Endpoint.OnReceiveDataListener receiveDataClient2
            = mock(Endpoint.OnReceiveDataListener.class);
    private final Endpoint.OnConnectionErrorListener connectionErrorClient2
            = mock(Endpoint.OnConnectionErrorListener.class);

    private final Endpoint.OnReceiveDataListener receiveDataClient3
            = mock(Endpoint.OnReceiveDataListener.class);
    private final Endpoint.OnConnectionErrorListener connectionErrorClient3
            = mock(Endpoint.OnConnectionErrorListener.class);

    public TestTCPServerMultipleClient() {
        parserMap.put(TestEventType.EVENT_TEST1, new TestEventData1.Parser());
    }

    @Test
    public void testNormal() throws IOException, InterruptedException {
        assertEquals(parserMap.size(), 1);
        Server server = serverFactory.create(port, parserMap);
        server.setAcceptErrorListener(serverAcceptError);

        Endpoint serverEndpoint = server.getEndpoint();
        serverEndpoint.setReceiveDataListener(receiveDataServer);
        serverEndpoint.setConnectionErrorListener(connectionErrorServer);

        Endpoint clientEndpoint1 = clientFactory1.connect(localhost, port, parserMap);
        clientEndpoint1.setReceiveDataListener(receiveDataClient1);
        clientEndpoint1.setConnectionErrorListener(connectionErrorClient1);

        Endpoint clientEndpoint2 = clientFactory2.connect(localhost, port, parserMap);
        clientEndpoint2.setReceiveDataListener(receiveDataClient2);
        clientEndpoint2.setConnectionErrorListener(connectionErrorClient2);

        Endpoint clientEndpoint3 = clientFactory3.connect(localhost, port, parserMap);
        clientEndpoint3.setReceiveDataListener(receiveDataClient3);
        clientEndpoint3.setConnectionErrorListener(connectionErrorClient3);

        clientEndpoint1.send(testEvent1);
        Thread.currentThread().sleep(30);
        clientEndpoint2.send(testEvent2);
        Thread.currentThread().sleep(50);

        server.shutdown();
        serverEndpoint.shutdown();
        clientEndpoint1.shutdown();
        clientEndpoint2.shutdown();
        clientEndpoint3.shutdown();

        serverEventManager.update();
        clientEventManager1.update();
        clientEventManager2.update();
        clientEventManager3.update();

        ArgumentCaptor<EventData> serverCaptor = ArgumentCaptor.forClass(EventData.class);
        ArgumentCaptor<EventData> clientCaptor1 = ArgumentCaptor.forClass(EventData.class);
        ArgumentCaptor<EventData> clientCaptor2 = ArgumentCaptor.forClass(EventData.class);
        ArgumentCaptor<EventData> clientCaptor3 = ArgumentCaptor.forClass(EventData.class);

        verify(receiveDataServer, times(2)).onReceiveData(serverCaptor.capture());
        List<EventData> serverEventList = serverCaptor.getAllValues();
        assertEquals(serverEventList.size(), 2);
        assertEventEquals(serverEventList.get(0), testEvent1);
        assertEventEquals(serverEventList.get(1), testEvent2);

        verify(receiveDataClient1, times(1)).onReceiveData(clientCaptor1.capture());
        List<EventData> clientEventList1 = clientCaptor1.getAllValues();
        assertEquals(clientEventList1.size(), 1);
        assertEventEquals(clientEventList1.get(0), testEvent2);

        verify(receiveDataClient3, times(2)).onReceiveData(clientCaptor3.capture());
        List<EventData> clientEventList3 = clientCaptor3.getAllValues();
        assertEquals(clientEventList3.size(), 2);
        assertEventEquals(clientEventList3.get(0), testEvent1);
        assertEventEquals(clientEventList3.get(1), testEvent2);

        verify(serverAcceptError, times(1)).onAcceptError();
        verify(connectionErrorServer, times(3)).onConnectionError((Connection) any());
        verify(connectionErrorClient1, times(1)).onConnectionError((Connection) any());
        verify(connectionErrorClient2, times(1)).onConnectionError((Connection) any());
        verify(connectionErrorClient3, times(1)).onConnectionError((Connection) any());
    }

    private void assertEventEquals(EventData expected ,TestEventData1 actual) {
        TestEventData1 event = (TestEventData1) expected;
        assertEquals(event.id(), actual.id());
        assertEquals(event.name(), actual.name());
    }
}
