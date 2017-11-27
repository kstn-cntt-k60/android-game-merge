package kstn.game.logic.playing_event.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
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

    @Override
    public void serialize(OutputStream out) throws IOException {
        RoomMessage.RequestJoinRoom msg = RoomMessage.RequestJoinRoom.newBuilder()
                .setClientIpAddress(clientIpAddress)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            RoomMessage.RequestJoinRoom msg
                    = RoomMessage.RequestJoinRoom.parseDelimitedFrom(in);
            return new RequestJoinRoomEvent(msg.getClientIpAddress());
        }
    }
}
