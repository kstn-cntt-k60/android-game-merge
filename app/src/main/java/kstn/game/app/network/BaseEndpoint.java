package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventType;
import kstn.game.app.event.LLEventData;
import kstn.game.app.event.LLEventManager;
import kstn.game.app.event.LLListener;
import kstn.game.app.network.events.TCPConnectionError;
import kstn.game.app.network.events.TCPReceiveData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class BaseEndpoint implements Endpoint {
    private final List<BaseConnection> connections = new CopyOnWriteArrayList<>();
    private final LLEventManager llEventManager;
    private final Map<Integer, EventData.Parser> parserMap = new HashMap<>();

    private OnConnectionErrorListener connectionErrorListener = null;
    private OnReceiveDataListener receiveDataListener = null;

    private final LLListener connectionErrorLLListener;
    private final LLListener receiveDataLLListener;


    public BaseEndpoint(LLEventManager llEventManager,
                        Map<EventType, EventData.Parser> parserMap) {
        this.llEventManager = llEventManager;
        for (Map.Entry<EventType, EventData.Parser> entry: parserMap.entrySet()) {
            this.parserMap.put(entry.getKey().getValue(), entry.getValue());
        }

        connectionErrorLLListener = new LLListener() {
            @Override
            public void onEvent(LLEventData event) {
                Connection connection = ((TCPConnectionError) event).getConnection();
                BaseConnection baseConnection = (BaseConnection)connection;
                baseConnection.shutdown();
                removeConnection(baseConnection);

                if (connectionErrorListener != null) {
                    connectionErrorListener.onConnectionError(connection);
                }
            }
        };

        receiveDataLLListener = new LLListener() {
            @Override
            public void onEvent(LLEventData event) {
                EventData eventData = ((TCPReceiveData) event).getEvent();
                if (receiveDataListener != null) {
                    receiveDataListener.onReceiveData(eventData);
                }
            }
        };

        llEventManager.addListener(LLBaseEventType.TCP_CONNECTION_ERROR, connectionErrorLLListener);
        llEventManager.addListener(LLBaseEventType.TCP_RECEIVE_DATA, receiveDataLLListener);
    }

    class BaseConnection implements Connection, Runnable {
        private final Thread thread;
        private final Socket socket;
        private volatile boolean running = false;

        private InputStream inputStream = null;
        private OutputStream outputStream = null;
        private DataInputStream eventIdReader = null;
        private DataOutputStream eventIdWriter = null;

        BaseConnection(Socket socket) {
            this.socket = socket;
            this.thread = new Thread(this);
        }

        public void start() {
            try {
                inputStream = socket.getInputStream();
                inputStream = new BufferedInputStream(inputStream);
                outputStream = socket.getOutputStream();
                outputStream = new BufferedOutputStream(outputStream);
                eventIdReader = new DataInputStream(inputStream);
                eventIdWriter = new DataOutputStream(outputStream);
            } catch (IOException e) {
                llEventManager.queue(new TCPConnectionError(this));
                return;
            }
            this.thread.start();
        }

        @Override
        public void run() {
            if (inputStream == null || outputStream == null)
                return;

            this.running = true;
            while (running) {
                try {
                    int eventId = eventIdReader.readInt();
                    EventData.Parser parser = parserMap.get(eventId);
                    if (parser == null)
                        throw new RuntimeException("Can't have null parser");
                    EventData event = parser.parseFrom(inputStream);
                    event.setConnection(this);
                    llEventManager.queue(new TCPReceiveData(event));

                    // Distribute to other connections
                    BaseEndpoint.this.sendOtherConnections(this, event);
                } catch (IOException e) {
                    llEventManager.queue(new TCPConnectionError(this));
                    this.running = false;
                    return;
                }
            }
        }

        private void send(EventData event) {
            if (eventIdWriter == null)
                return;
            try {
                eventIdWriter.writeInt(event.getEventType().getValue());
                event.serialize(outputStream);
                eventIdWriter.flush();
            } catch (IOException e) {
                llEventManager.queue(new TCPConnectionError(this));
                this.running = false;
            }
        }

        public void shutdown() {
            this.running = false;
            if (inputStream != null && outputStream != null) {
                try {
                    this.inputStream.close();
                    this.outputStream.close();
                } catch (IOException e) {
                }
            }

            try {
                socket.close();
            } catch (IOException e) {
            }

            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    private void sendOtherConnections(BaseConnection conn, EventData event) {
        for (BaseConnection connection: connections) {
            if (conn != connection)
                connection.send(event);
        }
    }

    @Override
    public void send(EventData event) {
        for (BaseConnection conn: connections)
            conn.send(event);
    }

    @Override
    public void setConnectionErrorListener(OnConnectionErrorListener listener) {
        this.connectionErrorListener = listener;
    }

    @Override
    public void setReceiveDataListener(OnReceiveDataListener listener) {
        this.receiveDataListener = listener;
    }

    @Override
    public void shutdown() {
        llEventManager.removeListener(LLBaseEventType.TCP_CONNECTION_ERROR, connectionErrorLLListener);
        llEventManager.removeListener(LLBaseEventType.TCP_RECEIVE_DATA, receiveDataLLListener);

        for (BaseConnection conn: connections) {
            conn.shutdown();
        }
        connections.clear();
    }

    void addConnection(Socket socket) {
        BaseConnection connection = new BaseConnection(socket);
        connections.add(connection);
        connection.start();
    }

    void removeConnection(BaseConnection connection) {
        connections.remove(connection);
    }
}
