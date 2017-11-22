package kstn.game.logic.cone;

import kstn.game.logic.event.GameEventData;

public class ConeMoveEventData extends GameEventData {
    final private float angle;

    public ConeMoveEventData(float angle) {
        super(ConeEventType.MOVE);
        this.angle = angle;
    }

    float getAngle() {
        return angle;
    }

}
