package kstn.game.logic.state.multiplayer;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.playing_event.room.RoomMessage;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.state.IEntryExit;

public class ThisRoom implements IEntryExit {
    private String roomName = "";
    private int ipAddress = 0;
    private List<Player> playerList = new ArrayList<>();

    private final EventManager eventManager;

    private final EventListener setRoomListener;
    private final EventListener acceptJoinRoomListener;
    private final EventListener exitRoomListener;

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
                playerList.addAll(event1.getOldPlayers());
                playerList.add(event1.getNewPlayer());
            }
        };

        exitRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ExitRoomEvent event1 = (ExitRoomEvent) event;
                Iterator<Player> it = playerList.iterator();
                while (it.hasNext()) {
                    Player player = it.next();
                    if (player.getIpAddress() == event1.getPlayerIpAddress()) {
                        it.remove();
                        break;
                    }
                }
            }
        };
    }

    public void clear() {
        roomName = "";
        ipAddress = 0;
        playerList.clear();
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SET_THIS_ROOM, setRoomListener);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
        eventManager.addListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
        eventManager.removeListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
        eventManager.removeListener(PlayingEventType.SET_THIS_ROOM, setRoomListener);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
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
