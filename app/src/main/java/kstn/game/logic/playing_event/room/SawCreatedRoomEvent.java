package kstn.game.logic.playing_event.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
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

    @Override
    public void serialize(OutputStream out) throws IOException {
        RoomMessage.SawCreatedRoom msg = RoomMessage.SawCreatedRoom.newBuilder()
                .setIpAddress(ipAddress)
                .setRoomName(roomName)
                .setPlayerCount(playerCount)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            RoomMessage.SawCreatedRoom msg = RoomMessage.SawCreatedRoom.parseDelimitedFrom(in);
            return new SawCreatedRoomEvent(
                    msg.getIpAddress(), msg.getRoomName(), msg.getPlayerCount());
        }
    }
}
