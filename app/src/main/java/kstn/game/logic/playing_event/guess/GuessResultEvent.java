package kstn.game.logic.playing_event.guess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class GuessResultEvent extends GameEventData {
    private final String result;

    public GuessResultEvent(String result) {
        super(PlayingEventType.GUESS_RESULT);
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        GuessMessage.GuessResult msg = GuessMessage.GuessResult.newBuilder()
                .setResult(result)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            GuessMessage.GuessResult msg = GuessMessage.GuessResult.parseDelimitedFrom(in);
            return new GuessResultEvent(msg.getResult());
        }
    }
}
