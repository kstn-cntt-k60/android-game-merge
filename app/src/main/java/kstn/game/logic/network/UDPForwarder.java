package kstn.game.logic.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;

public class UDPForwarder implements IUDPForwarder {
    private final EventManager eventManager;
    private final UDPManagerFactory factory;
    private final WifiInfo wifiInfo;
    private final int port = 2017;
    private UDPManager manager;
    private Map<EventType, EventData.Parser> parserMap = new HashMap<>();
    private EventListener listener;

    public UDPForwarder(final EventManager eventManager,
                        UDPManagerFactory factory,
                        WifiInfo wifiInfo) {
        this.eventManager = eventManager;
        this.factory = factory;
        this.wifiInfo = wifiInfo;
        manager = null;

        parserMap.put(PlayingEventType.SAW_CREATED_ROOM,
                new SawCreatedRoomEvent.Parser());

        listener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                sendEvent(event);
            }
        };
    }

    private void sendEvent(EventData event) {
        if (manager != null)
            manager.broadcast(event);
    }

    @Override
    public void listen() throws IOException {
        manager = factory.create(
                wifiInfo.getIP(), port, wifiInfo.getMask(), parserMap);
        if (manager != null) {
            eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, listener);
        }
    }

    @Override
    public void shutdown() {
        if (manager != null) {
            eventManager.removeListener(PlayingEventType.SAW_CREATED_ROOM, listener);
            manager.shutdown();
        }
        manager = null;
    }
}
