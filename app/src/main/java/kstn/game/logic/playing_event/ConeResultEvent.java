package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class ConeResultEvent extends GameEventData {
    private int result;

    public ConeResultEvent(int result) {
        super(PlayingEventType.CONE_RESULT);
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.ConeResult msg = PlayingMessage.ConeResult.newBuilder()
                .setResult(result)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.ConeResult msg = PlayingMessage.ConeResult.parseDelimitedFrom(in);
            return new ConeResultEvent(msg.getResult());
        }
    }
}
