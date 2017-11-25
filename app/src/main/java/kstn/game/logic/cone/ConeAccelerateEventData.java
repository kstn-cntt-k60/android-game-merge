package kstn.game.logic.cone;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;

public class ConeAccelerateEventData extends GameEventData {
    final private float angle;
    final private float speedStart ;

    public ConeAccelerateEventData(float angle, float speedStart) {
        super(ConeEventType.ACCELERATE);
        this.angle = angle;
        this.speedStart = speedStart;
    }

    public float getAngle() {
        return angle;
    }

    public float getSpeedStart() { return speedStart; }

    @Override
    public void serialize(OutputStream out) throws IOException {
        ConeMessage.ConeAccelerate msg = ConeMessage.ConeAccelerate.newBuilder()
                .setAngle(angle)
                .setSpeedStart(speedStart)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            ConeMessage.ConeAccelerate msg =
                    ConeMessage.ConeAccelerate.parseDelimitedFrom(in);
            return new ConeAccelerateEventData(msg.getAngle(), msg.getSpeedStart());
        }
    }
}
