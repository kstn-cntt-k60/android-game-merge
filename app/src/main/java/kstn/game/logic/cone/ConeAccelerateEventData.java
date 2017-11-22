package kstn.game.logic.cone;

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
}
