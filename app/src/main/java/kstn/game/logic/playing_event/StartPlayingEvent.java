package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class StartPlayingEvent extends GameEventData {

    public StartPlayingEvent() {
        super(PlayingEventType.START_PLAYING);
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.StartPlaying msg = PlayingMessage.StartPlaying.newBuilder()
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.StartPlaying msg = PlayingMessage.StartPlaying.parseDelimitedFrom(in);
            return new StartPlayingEvent();
        }
    }
}
