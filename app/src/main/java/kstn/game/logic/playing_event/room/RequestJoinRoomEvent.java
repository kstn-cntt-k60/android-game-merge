package kstn.game.logic.playing_event.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.multiplayer.Player;

public class RequestJoinRoomEvent extends GameEventData {
    private final Player clientPlayer;

    public RequestJoinRoomEvent(int ip, String name, int avatarId) {
        super(PlayingEventType.REQUEST_JOIN_ROOM);
        clientPlayer = new Player(ip, name, avatarId);
    }

    public Player getClientPlayer() {
        return clientPlayer;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        RoomMessage.Player player = RoomMessage.Player.newBuilder()
                .setIpAddress(clientPlayer.getIpAddress())
                .setName(clientPlayer.getName())
                .setAvatarId(clientPlayer.getAvatarId())
                .build();

        RoomMessage.RequestJoinRoom msg = RoomMessage.RequestJoinRoom.newBuilder()
                .setClientPlayer(player)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            RoomMessage.RequestJoinRoom msg
                    = RoomMessage.RequestJoinRoom.parseDelimitedFrom(in);
            RoomMessage.Player player = msg.getClientPlayer();
            return new RequestJoinRoomEvent(
                    player.getIpAddress(), player.getName(), player.getAvatarId()
            );
        }
    }
}
