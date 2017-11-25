package kstn.game.logic.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kstn.game.logic.cone.ConeAccelerateEventData;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeMoveEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;

public class NetworkForwarder implements Endpoint.OnReceiveDataListener {
    private final EventManager eventManager;
    private final ServerFactory serverFactory;
    private final ClientFactory clientFactory;
    private final int port = 2017;
    final Map<EventType, EventData.Parser> parserMap = new HashMap<>();
    private Endpoint endpoint = null;
    private Server server = null;
    private EventListener listener;
    private boolean isReceiving = false;

    public NetworkForwarder(EventManager eventManager,
                            ServerFactory serverFactory,
                            ClientFactory clientFactory) {
        this.eventManager = eventManager;
        this.serverFactory = serverFactory;
        this.clientFactory = clientFactory;

        listener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                if (!isReceiving)
                    endpoint.send(event);
            }
        };
        initParserMap();
    }

    private void initParserMap() {
        // Cone
        parserMap.put(ConeEventType.MOVE, new ConeMoveEventData.Parser());
        parserMap.put(ConeEventType.ACCELERATE, new ConeAccelerateEventData.Parser());
    }

    private void addListeners() {
        for (EventType eventType: parserMap.keySet()) {
            eventManager.addListener(eventType, listener);
        }
    }

    private void removeListeners() {
        for (EventType eventType: parserMap.keySet()) {
            eventManager.removeListener(eventType, listener);
        }
    }

    public void setOnConnectionErrorListener(Endpoint.OnConnectionErrorListener listener) {
        if (endpoint != null)
            endpoint.setConnectionErrorListener(listener);
    }

    public void setOnAcceptErrorListener(Server.OnAcceptErrorListener listener) {
        if (server != null) {
            server.setAcceptErrorListener(listener);
        }
    }

    public void listen() throws IOException {
        if (endpoint != null)
            throw new RuntimeException("Can't listen with working endpoint");

        server = serverFactory.create(port, parserMap);
        endpoint = server.getEndpoint();
        if (endpoint != null) {
            isReceiving = false;
            endpoint.setReceiveDataListener(this);
            addListeners();
        }
    }

    public void connect(int ipAddress) throws IOException {
        if (endpoint != null)
            throw new RuntimeException("Can't listen with working endpoint");

        endpoint = clientFactory.connect(ipAddress, port, parserMap);
        if (endpoint != null) {
            isReceiving = false;
            endpoint.setReceiveDataListener(this);
            addListeners();
        }
    }

    @Override
    public void onReceiveData(EventData event) {
        isReceiving = true;
        eventManager.trigger(event);
        isReceiving = false;
    }

    public void shutdown() {
        if (endpoint != null) {
            removeListeners();
            endpoint.shutdown();
        }

        if (server != null) {
            server.shutdown();
        }

        endpoint = null;
        server = null;
    }
}
