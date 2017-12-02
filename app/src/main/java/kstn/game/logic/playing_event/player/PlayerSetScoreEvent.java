package kstn.game.logic.playing_event.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerSetScoreEvent extends GameEventData {
    private final int score;

    public PlayerSetScoreEvent(int score) {
        super(PlayingEventType.PLAYER_SET_SCORE);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PlayerSetScoreEvent) {
            PlayerSetScoreEvent event = (PlayerSetScoreEvent) other;
            return this.score == event.score;
        }
        return false;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayerMessage.PlayerSetScore msg = PlayerMessage.PlayerSetScore
                .newBuilder()
                .setScore(score)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayerMessage.PlayerSetScore msg = PlayerMessage.PlayerSetScore
                    .parseDelimitedFrom(in);
            return new PlayerSetScoreEvent(msg.getScore());
        }
    }
}
