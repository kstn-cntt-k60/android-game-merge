package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class SetThisRoomEvent extends GameEventData {
    private final String roomName;
    private final int ipAddress;

    public SetThisRoomEvent(String roomName, int ipAddress) {
        super(PlayingEventType.SET_THIS_ROOM);
        this.roomName = roomName;
        this.ipAddress = ipAddress;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getIpAddress() {
        return ipAddress;
    }
}
