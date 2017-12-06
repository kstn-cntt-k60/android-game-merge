package kstn.game.logic.playing_event.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerDeactivateEvent extends GameEventData {
    private final int playerIndex;

    public PlayerDeactivateEvent(int playerIndex) {
        super(PlayingEventType.PLAYER_DEACTIVATE);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof  PlayerDeactivateEvent) {
            PlayerDeactivateEvent other = (PlayerDeactivateEvent) object;
            return other.playerIndex == playerIndex;
        }
        return false;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayerMessage.PlayerDeactivate msg = PlayerMessage.PlayerDeactivate.newBuilder()
                .setPlayerIndex(playerIndex)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayerMessage.PlayerDeactivate msg = PlayerMessage.PlayerDeactivate
                    .parseDelimitedFrom(in);
            return new PlayerDeactivateEvent(msg.getPlayerIndex());
        }
    }
}
