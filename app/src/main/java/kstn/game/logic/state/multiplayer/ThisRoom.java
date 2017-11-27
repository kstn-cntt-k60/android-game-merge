package kstn.game.logic.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.state.IEntryExit;

public class ThisRoom implements IEntryExit {
    private String roomName = "";
    private int ipAddress = 0;
    private List<Player> playerList = new ArrayList<>();

    private final EventListener setRoomListener;
    private final EventListener acceptJoinRoomListener;
    private final EventManager eventManager;

    public ThisRoom(EventManager eventManager) {
        this.eventManager = eventManager;

        setRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SetThisRoomEvent event1 = (SetThisRoomEvent) event;
                roomName = event1.getRoomName();
                ipAddress = event1.getIpAddress();
            }
        };

        acceptJoinRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                AcceptJoinRoomEvent event1 = (AcceptJoinRoomEvent) event;
                playerList.clear();
                for (Player player: event1.getOldPlayers())
                    playerList.add(player);
                playerList.add(event1.getNewPlayer());
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SET_THIS_ROOM, setRoomListener);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
        eventManager.removeListener(PlayingEventType.SET_THIS_ROOM, setRoomListener);
    }

    public String getRoomName() {
        return roomName;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
