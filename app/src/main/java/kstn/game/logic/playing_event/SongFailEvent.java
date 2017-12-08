package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class SongFailEvent extends GameEventData {

    public SongFailEvent() {
        super(PlayingEventType.SONG_FAIL);
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.SongFail msg = PlayingMessage.SongFail.newBuilder()
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.SongFail msg = PlayingMessage.SongFail.parseDelimitedFrom(in);
            return new SongFailEvent();
        }
    }
}
