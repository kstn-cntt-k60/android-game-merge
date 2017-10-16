package kstn.game.view.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/10/2017.
 */

public class ConeMoveEventData extends BaseEventData {
    final private float angle;

    public ConeMoveEventData(float angle) {
        super(0);
        this.angle = angle;
    }

    float getAngle() {
        return angle;
    }

    @Override
    public EventType getEventType() {
        return ConeEventType.MOVE;
    }

    @Override
    public String getName() {
        return "";
    }
}
