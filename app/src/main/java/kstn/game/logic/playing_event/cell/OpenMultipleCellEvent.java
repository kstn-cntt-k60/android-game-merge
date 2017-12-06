package kstn.game.logic.playing_event.cell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class OpenMultipleCellEvent extends GameEventData {
    private final char character;

    public OpenMultipleCellEvent(char ch) {
        super(PlayingEventType.OPEN_MULTIPLE_CELL);
        this.character = ch;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        CellMessage.OpenMultipleCell msg =
                CellMessage.OpenMultipleCell.newBuilder()
                .setCharacter(character)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            CellMessage.OpenMultipleCell msg =
                    CellMessage.OpenMultipleCell.parseDelimitedFrom(in);
            return new OpenMultipleCellEvent((char) msg.getCharacter());
        }
    }
}
