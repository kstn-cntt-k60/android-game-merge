package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class SawCreatedRoomEvent extends GameEventData {
    private final int ipAddress;
    private final String roomName;
    private final int playerCount;

    public SawCreatedRoomEvent(int ipAddress, String roomName, int playerCount) {
        super(PlayingEventType.SAW_CREATED_ROOM);
        this.ipAddress = ipAddress;
        this.roomName = roomName;
        this.playerCount = playerCount;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
