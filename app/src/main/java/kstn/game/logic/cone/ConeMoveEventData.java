package kstn.game.logic.cone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class ConeMoveEventData extends GameEventData {
    final private float angle;

    public ConeMoveEventData(float angle) {
        super(ConeEventType.MOVE);
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        ConeMessage.ConeMove msg = ConeMessage.ConeMove.newBuilder()
                .setAngle(angle)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            ConeMessage.ConeMove msg = ConeMessage.ConeMove.parseDelimitedFrom(in);
            return new ConeMoveEventData(msg.getAngle());
        }
    }

}
