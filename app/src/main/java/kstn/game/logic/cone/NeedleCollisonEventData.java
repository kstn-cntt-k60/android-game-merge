package kstn.game.logic.cone;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 12/11/2017.
 */

public class NeedleCollisonEventData extends BaseEventData {
    final private float accerlator;
    final private float speed;

    public NeedleCollisonEventData(float speed, float accerlator) {
        super(0);
        this.accerlator = accerlator;
        this.speed = speed;
    }

    public float getAccerlator() {
        return accerlator;
    }
    public float getSpeed() { return speed; }
    @Override
    public EventType getEventType() {
        return NeedleEventType.COLLISION;
    }

    @Override
    public String getName() {
        return "";
    }
}
