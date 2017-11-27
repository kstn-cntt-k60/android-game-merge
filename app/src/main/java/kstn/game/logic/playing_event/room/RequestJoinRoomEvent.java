package kstn.game.logic.playing_event.room;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class RequestJoinRoomEvent extends GameEventData {
    private final int clientIpAddress;

    public RequestJoinRoomEvent(int clientIpAddress) {
        super(PlayingEventType.REQUEST_JOIN_ROOM);
        this.clientIpAddress = clientIpAddress;
    }

    public int getClientIpAddress() {
        return clientIpAddress;
    }
}
