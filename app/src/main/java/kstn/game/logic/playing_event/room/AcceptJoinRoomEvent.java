package kstn.game.logic.playing_event.room;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.multiplayer.Player;

public class AcceptJoinRoomEvent extends GameEventData {
    private final Player newPlayer;
    private final List<Player> oldPlayers;

    public AcceptJoinRoomEvent(Player newPlayer, List<Player> oldPlayers) {
        super(PlayingEventType.ACCEPT_JOIN_ROOM);
        this.newPlayer = new Player(
                newPlayer.getIpAddress(),
                newPlayer.getName(),
                newPlayer.getAvatarId()
        );

        this.oldPlayers = new ArrayList<>();
        for (Player oldPlayer: oldPlayers) {
            this.oldPlayers.add(new Player(
                    oldPlayer.getIpAddress(),
                    oldPlayer.getName(),
                    oldPlayer.getAvatarId()
            ));
        }
    }

    public Player getNewPlayer() {
        return newPlayer;
    }

    public List<Player> getOldPlayers() {
        return oldPlayers;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        RoomMessage.Player msgNewPlayer = RoomMessage.Player.newBuilder()
                .setIpAddress(newPlayer.getIpAddress())
                .setName(newPlayer.getName())
                .setAvatarId(newPlayer.getAvatarId())
                .build();

        RoomMessage.AcceptJoinRoom.Builder builder
                = RoomMessage.AcceptJoinRoom.newBuilder();
        builder.setNewPlayer(msgNewPlayer);
        for (Player oldPlayer: oldPlayers) {
            RoomMessage.Player msgOldPlayer = RoomMessage.Player.newBuilder()
                    .setIpAddress(oldPlayer.getIpAddress())
                    .setName(oldPlayer.getName())
                    .setAvatarId(oldPlayer.getAvatarId())
                    .build();
            builder.addOldPlayers(msgOldPlayer);
        }
        builder.build().writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            RoomMessage.AcceptJoinRoom msg = RoomMessage.AcceptJoinRoom.parseDelimitedFrom(in);

            List<Player> oldPlayerList = new ArrayList<>();
            Player newPlayer = msgPlayerToPlayer(msg.getNewPlayer());
            for (RoomMessage.Player msgOldPlayer: msg.getOldPlayersList()) {
                Player oldPlayer = msgPlayerToPlayer(msgOldPlayer);
                oldPlayerList.add(oldPlayer);
            }
            return new AcceptJoinRoomEvent(newPlayer, oldPlayerList);
        }

        private Player msgPlayerToPlayer(RoomMessage.Player msgPlayer) {
            return new Player(msgPlayer.getIpAddress(),
                    msgPlayer.getName(), msgPlayer.getAvatarId());
        }
    }
}
