package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class SongTingTingEvent extends GameEventData {
    public SongTingTingEvent() {
        super(PlayingEventType.SONG_TINGTING);
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.SongTingTing msg = PlayingMessage.SongTingTing.newBuilder()
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.SongTingTing msg = PlayingMessage.SongTingTing.parseDelimitedFrom(in);
            return new SongTingTingEvent();
        }
    }
}
