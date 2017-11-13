package kstn.game.logic.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 08/11/2017.
 */

public class ConeAccelerateEventData extends BaseEventData{
    final private float angle;
    final private float speedStart ;

    public ConeAccelerateEventData(float angle, float speedStart) {
        super(0);
        this.angle = angle;
        this.speedStart = speedStart;
    }

    float getAngle() {
        return angle;
    }
    float getSpeedStart() { return speedStart; }

    @Override
    public EventType getEventType() {
        return ConeEventType.ACCELERATE;
    }

    @Override
    public String getName() {
        return "";
    }
}
