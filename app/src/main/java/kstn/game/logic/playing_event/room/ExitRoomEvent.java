package kstn.game.logic.playing_event.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class ExitRoomEvent extends GameEventData {
    private final int playerIpAddress;

    public ExitRoomEvent(int playerIpAddress) {
        super(PlayingEventType.EXIT_ROOM);
        this.playerIpAddress = playerIpAddress;
    }

    public int getPlayerIpAddress() {
        return playerIpAddress;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        RoomMessage.ExitRoom exitRoom = RoomMessage.ExitRoom.newBuilder()
                .setPlayerIpAddress(playerIpAddress)
                .build();
        exitRoom.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {

        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            RoomMessage.ExitRoom exitRoom
                    = RoomMessage.ExitRoom.parseDelimitedFrom(in);
            return new ExitRoomEvent(exitRoom.getPlayerIpAddress());
        }
    }
}
