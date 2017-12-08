package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class ShowToastEvent extends GameEventData {
    private final String text;

    public ShowToastEvent(String text) {
        super(PlayingEventType.SHOW_TOAST);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.ShowToast msg = PlayingMessage.ShowToast.newBuilder()
                .setText(text)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.ShowToast msg = PlayingMessage.ShowToast.parseDelimitedFrom(in);
            return new ShowToastEvent(msg.getText());
        }
    }
}
