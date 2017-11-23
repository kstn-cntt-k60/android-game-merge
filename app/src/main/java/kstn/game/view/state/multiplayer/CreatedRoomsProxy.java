package kstn.game.view.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.state.IEntryExit;

public class CreatedRoomsProxy implements IEntryExit {
    private final EventManager eventManager;
    private final ICreatedRooms createdRooms;

    private EventListener sawCreatedRoomListener;
    private EventListener removeCreatedRoomListener;

    public CreatedRoomsProxy(EventManager eventManager, ICreatedRooms manager) {
        this.eventManager = eventManager;
        this.createdRooms = manager;

        sawCreatedRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SawCreatedRoomEvent event1 = (SawCreatedRoomEvent) event;
                createdRooms.addRoom(event1.getIpAddress(),
                        event1.getRoomName(), event1.getPlayerCount());
            }
        };

        removeCreatedRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                RemoveCreatedRoomEvent event1 = (RemoveCreatedRoomEvent) event;
                createdRooms.remoteRoom(event1.getIpAddress());
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, sawCreatedRoomListener);
        eventManager.addListener(PlayingEventType.REMOVE_CREATED_ROOM, removeCreatedRoomListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.REMOVE_CREATED_ROOM, removeCreatedRoomListener);
        eventManager.removeListener(PlayingEventType.SAW_CREATED_ROOM, sawCreatedRoomListener);
    }
}
