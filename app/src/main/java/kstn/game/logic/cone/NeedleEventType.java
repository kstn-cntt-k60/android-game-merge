package kstn.game.logic.cone;

import kstn.game.logic.event.EventType;

/**
 * Created by qi on 04/11/2017.
 */

public enum NeedleEventType implements EventType {
    MOVE(1000),
    COLLISION(1001);

    private int value;
    NeedleEventType(int value) {this.value = value;}

    @Override
    public int getValue() {
        return value;
    }
}
