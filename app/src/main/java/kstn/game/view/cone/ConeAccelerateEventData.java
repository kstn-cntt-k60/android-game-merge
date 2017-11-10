package kstn.game.view.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 08/11/2017.
 */

public class ConeAccelerateEventData extends BaseEventData{
    final private float angle;

    public ConeAccelerateEventData(float angle) {
        super(0);
        this.angle = angle;
    }

    float getAngle() {
        return angle;
    }

    @Override
    public EventType getEventType() {
        return ConeEventType.ACCELERATE;
    }

    @Override
    public String getName() {
        return "";
    }
}
