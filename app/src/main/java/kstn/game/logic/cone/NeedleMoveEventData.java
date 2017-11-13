package kstn.game.logic.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 04/11/2017.
 */

public class NeedleMoveEventData extends BaseEventData {
    final private float x;
    final private float y;

    public NeedleMoveEventData(float x, float y) {
        super(0);
        this.x = x;
        this.y = y;
    }

    float getX() {
        return x;
    }
    float getY() { return y; }

    @Override
    public EventType getEventType() {
        return NeedleEventType.MOVE;
    }

    @Override
    public String getName() {
        return "";
    }
}
