package kstn.game.logic.playing_event.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class NextPlayerEvent extends GameEventData {
    private final int playerIndex;

    public NextPlayerEvent(int playerIndex) {
        super(PlayingEventType.NEXT_PLAYER);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof  NextPlayerEvent) {
            NextPlayerEvent other = (NextPlayerEvent) object;
            return playerIndex == other.playerIndex;
        }
        return false;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayerMessage.NextPlayer msg = PlayerMessage.NextPlayer.newBuilder()
                .setPlayerIndex(playerIndex)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayerMessage.NextPlayer msg = PlayerMessage.NextPlayer
                    .parseDelimitedFrom(in);
            return new NextPlayerEvent(msg.getPlayerIndex());
        }
    }
}
