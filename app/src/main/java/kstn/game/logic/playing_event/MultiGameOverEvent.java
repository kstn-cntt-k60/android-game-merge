package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class MultiGameOverEvent extends GameEventData {
    public MultiGameOverEvent() {
        super(PlayingEventType.MULTI_GAME_OVER);
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.MultiGameOver msg
                = PlayingMessage.MultiGameOver.newBuilder()
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.MultiGameOver msg
                    = PlayingMessage.MultiGameOver.parseDelimitedFrom(in);
            return new MultiGameOverEvent();
        }
    }
}
