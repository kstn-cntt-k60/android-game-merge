package kstn.game.logic.playing_event.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerActivateEvent extends GameEventData {
    private final int playerIndex;

    public PlayerActivateEvent(int playerIndex) {
        super(PlayingEventType.PLAYER_ACTIVATE);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerActivateEvent) {
            PlayerActivateEvent other = (PlayerActivateEvent) o;
            return this.playerIndex == other.playerIndex;
        }
        return false;
    }


    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayerMessage.PlayerActivate msg = PlayerMessage.PlayerActivate.newBuilder()
                .setPlayerIndex(playerIndex)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayerMessage.PlayerActivate msg = PlayerMessage.PlayerActivate.parseDelimitedFrom(in);
            return new PlayerActivateEvent(msg.getPlayerIndex());
        }
    }
}
