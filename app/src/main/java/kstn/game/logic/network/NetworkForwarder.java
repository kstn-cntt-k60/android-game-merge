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
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.answer.AnswerEvent;
import kstn.game.logic.playing_event.cell.OpenCellEvent;
import kstn.game.logic.playing_event.cell.OpenMultipleCellEvent;
import kstn.game.logic.playing_event.guess.GuessResultEvent;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerReadyEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;

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

        // Answer
        parserMap.put(PlayingEventType.ANSWER, new AnswerEvent.Parser());

        // Cell
        parserMap.put(PlayingEventType.OPEN_CELL, new OpenCellEvent.Parser());
        parserMap.put(PlayingEventType.OPEN_MULTIPLE_CELL, new OpenMultipleCellEvent.Parser());

        // Guess
        parserMap.put(PlayingEventType.GUESS_RESULT, new GuessResultEvent.Parser());

        // Player
        parserMap.put(PlayingEventType.NEXT_PLAYER, new NextPlayerEvent.Parser());
        parserMap.put(PlayingEventType.PLAYER_DEACTIVATE, new PlayerDeactivateEvent.Parser());
        parserMap.put(PlayingEventType.PLAYER_READY, new PlayerReadyEvent.Parser());
        parserMap.put(PlayingEventType.PLAYER_SET_SCORE, new PlayerSetScoreEvent.Parser());

        // Room
        parserMap.put(PlayingEventType.REQUEST_JOIN_ROOM, new RequestJoinRoomEvent.Parser());
        parserMap.put(PlayingEventType.ACCEPT_JOIN_ROOM, new AcceptJoinRoomEvent.Parser());
        parserMap.put(PlayingEventType.EXIT_ROOM, new ExitRoomEvent.Parser());
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
        shutdown();
        server = serverFactory.create(port, parserMap);
        endpoint = server.getEndpoint();
        if (endpoint != null) {
            isReceiving = false;
            endpoint.setReceiveDataListener(this);
            addListeners();
        }
    }

    public void connect(int ipAddress) throws IOException {
        shutdown();
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
