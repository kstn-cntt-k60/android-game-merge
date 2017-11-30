package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class ClickRoomEvent extends GameEventData {
    private final int ipAddress;
    private final String roomName;

    public ClickRoomEvent(int ipAddress, String roomName) {
        super(PlayingEventType.CLICK_ROOM);
        this.ipAddress = ipAddress;
        this.roomName = roomName;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public String getRoomName() {
        return roomName;
    }
}
