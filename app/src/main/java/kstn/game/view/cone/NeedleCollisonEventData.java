package kstn.game.view.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 12/11/2017.
 */

public class NeedleCollisonEventData extends BaseEventData {
    final private float angle;

    public NeedleCollisonEventData(float angle) {
        super(0);
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public EventType getEventType() {
        return NeedleEventType.COLLISION;
    }

    @Override
    public String getName() {
        return "";
    }
}
