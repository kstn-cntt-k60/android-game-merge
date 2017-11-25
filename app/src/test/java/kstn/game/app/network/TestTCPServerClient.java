package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventData.Parser;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.TestEventType;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.Server;
import kstn.game.logic.network.ServerFactory;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestTCPServerClient {
    private final LLBaseEventManager serverLLManager = new LLBaseEventManager();
    private final LLBaseEventManager clientLLManager = new LLBaseEventManager();
    private final ServerFactory serverFactory;
    private final ClientFactory clientFactory;
    private final String ip = "127.0.0.1";
    private final int localhost = NetworkUtil.ipStringToInt(ip);

    private Map<EventType, Parser> parserMap = new HashMap<>();

    private TestEventData1 event = new TestEventData1(103, 122, "Van");

    private Endpoint.OnReceiveDataListener receiveDataListener
            = mock(Endpoint.OnReceiveDataListener.class);
    private Endpoint.OnConnectionErrorListener connectionErrorListener
            = mock(Endpoint.OnConnectionErrorListener.class);

    public TestTCPServerClient() {
        serverFactory = new BaseServerFactory(serverLLManager);
        clientFactory = new BaseClientFactory(clientLLManager);

        parserMap.put(TestEventType.EVENT_TEST1, new TestEventData1.Parser());
    }

    @Test
    public void testNormal() throws IOException {
        Server server = serverFactory.create(2345, parserMap);
        Endpoint endpoint1 = server.getEndpoint();
        endpoint1.setConnectionErrorListener(connectionErrorListener);
        endpoint1.setReceiveDataListener(receiveDataListener);

        Endpoint endpoint2 = clientFactory.connect(localhost, 2345, parserMap);
        endpoint2.setConnectionErrorListener(connectionErrorListener);
        endpoint2.send(event);

        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
        }

        serverLLManager.update();
        clientLLManager.update();

        server.shutdown();
        endpoint1.shutdown();
        endpoint2.shutdown();

        ArgumentCaptor<EventData> captor1 = ArgumentCaptor.forClass(EventData.class);

        verify(connectionErrorListener, times(0)).onConnectionError((Connection) any());
        verify(receiveDataListener, times(1)).onReceiveData(captor1.capture());
        TestEventData1 receiveEvent = (TestEventData1) captor1.getValue();
        assertEquals(receiveEvent.id(), event.id());
        assertEquals(receiveEvent.name(), event.name());
    }


    @Test
    public void test_serialize_parser_addSomethingAtTheEnd() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TestEventData1 event = new TestEventData1(333,444, "TUNG");
        event.serialize(outputStream);

        outputStream.write(12);

        byte[] bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        TestEventData1 test = (TestEventData1) new TestEventData1.Parser().parseFrom(inputStream);
        assertEquals(test.id(), event.id());
        assertEquals(test.name(), event.name());
    }
}
