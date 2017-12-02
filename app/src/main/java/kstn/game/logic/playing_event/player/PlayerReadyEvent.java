package kstn.game.logic.playing_event.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerReadyEvent extends GameEventData {
    private final int ipAddress;

    public PlayerReadyEvent(int ipAddress) {
        super(PlayingEventType.PLAYER_READY);
        this.ipAddress = ipAddress;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayerMessage.PlayerReady msg = PlayerMessage.PlayerReady
                .newBuilder()
                .setIpAddress(ipAddress)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayerMessage.PlayerReady msg = PlayerMessage.PlayerReady
                    .parseDelimitedFrom(in);
            return new PlayerReadyEvent(msg.getIpAddress());
        }
    }
}
