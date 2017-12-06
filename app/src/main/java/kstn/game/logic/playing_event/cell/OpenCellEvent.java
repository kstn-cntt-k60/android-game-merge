package kstn.game.logic.playing_event.cell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class OpenCellEvent extends GameEventData {
    private final int index;

    public OpenCellEvent(int index) {
        super(PlayingEventType.OPEN_CELL);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        CellMessage.OpenCell msg = CellMessage.OpenCell.newBuilder()
                .setIndex(index)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            CellMessage.OpenCell msg = CellMessage.OpenCell.parseDelimitedFrom(in);
            return new OpenCellEvent(msg.getIndex());
        }
    }
}
