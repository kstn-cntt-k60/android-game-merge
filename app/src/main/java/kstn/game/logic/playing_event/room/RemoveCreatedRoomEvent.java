package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class RemoveCreatedRoomEvent extends GameEventData {
    private final int ipAddress;

    public RemoveCreatedRoomEvent(int ipAddress) {
        super(PlayingEventType.REMOVE_CREATED_ROOM);
        this.ipAddress = ipAddress;
    }

    public int getIpAddress() {
        return ipAddress;
    }
}
