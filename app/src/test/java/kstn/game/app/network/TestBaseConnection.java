package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.TestEventType;
import kstn.game.view.network.Connection;
import kstn.game.view.network.Endpoint;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestBaseConnection {
    private final Socket socket = mock(Socket.class);
    private ByteArrayInputStream byteInput = null;
    private ByteArrayOutputStream byteOutput = null;
    private final LLBaseEventManager llEventManager =
            new LLBaseEventManager();

    private final Map<EventType, EventData.Parser> parserMap = new HashMap<>();
    private final BaseEndpoint endpoint;
    private final BaseEndpoint.BaseConnection connection;

    private Endpoint.OnReceiveDataListener receiveListener
            = mock(Endpoint.OnReceiveDataListener.class);
    private Endpoint.OnConnectionErrorListener errorListener
            = mock(Endpoint.OnConnectionErrorListener.class);

    public TestBaseConnection() {
        parserMap.put(TestEventType.EVENT_TEST1, new TestEventData1.Parser());
        endpoint = new BaseEndpoint(llEventManager, parserMap);
        connection = endpoint. new BaseConnection(socket);

        endpoint.setReceiveDataListener(receiveListener);
        endpoint.setConnectionErrorListener(errorListener);
    }

    @Test
    public void testStart_whenGetOutputStreamThrow() throws IOException {
        byteInput = new ByteArrayInputStream(new byte[1024]);
        when(socket.getInputStream()).thenReturn(byteInput);
        when(socket.getOutputStream()).thenThrow(new IOException());
        connection.start();
        llEventManager.update();
        verify(errorListener, times(1)).onConnectionError((Connection) any());
        connection.shutdown();
    }

    @Test
    public void testStart_whenGetInputStreamThrow() throws IOException {
        byteOutput = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteOutput);
        when(socket.getInputStream()).thenThrow(new IOException());
        connection.start();
        llEventManager.update();
        verify(errorListener, times(1)).onConnectionError((Connection) any());
        connection.shutdown();
    }

    @Test
    public void testRunShutdown() throws IOException {
        byteOutput = new ByteArrayOutputStream();
        TestEventData1 event = new TestEventData1(123, 44, "TUNG");

        DataOutputStream eventIdWriter = new DataOutputStream(byteOutput);
        eventIdWriter.writeInt(event.getEventType().getValue());
        event.serialize(byteOutput);
        byte[] bytes = byteOutput.toByteArray();

        byteInput = new ByteArrayInputStream(bytes);
        byteOutput = new ByteArrayOutputStream();

        when(socket.getInputStream()).thenReturn(byteInput);
        when(socket.getOutputStream()).thenReturn(byteOutput);
        connection.start();
        try {
            Thread.currentThread().sleep(50);
        } catch (InterruptedException e) {
        }
        connection.shutdown();

        llEventManager.update();

        ArgumentCaptor<EventData> argument = ArgumentCaptor.forClass(EventData.class);
        verify(receiveListener, times(1)).onReceiveData(argument.capture());
        TestEventData1 receiveEvent = (TestEventData1) argument.getValue();
        assertEquals(receiveEvent.name(), event.name());
        assertEquals(receiveEvent.id(), event.id());
        assertEquals(receiveEvent.name(), "TUNG");

        ArgumentCaptor<Connection> captor2 = ArgumentCaptor.forClass(Connection.class);
        verify(errorListener, times(1)).onConnectionError(captor2.capture());
        Connection receiveConnection = captor2.getValue();
        assertEquals(receiveConnection, connection);
    }
}
