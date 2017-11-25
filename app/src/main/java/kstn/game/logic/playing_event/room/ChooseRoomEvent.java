package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class ChooseRoomEvent extends GameEventData {
    private final int ipAddress;
    private final String roomName;

    public ChooseRoomEvent(int ip, String roomName) {
        super(PlayingEventType.CHOOSE_ROOM_EVENT);
        this.ipAddress = ip;
        this.roomName = roomName;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public String getRoomName() {
        return roomName;
    }
}
