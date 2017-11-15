package kstn.game.logic.cone;

import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/10/2017.
 */

public enum ConeEventType implements EventType {
    MOVE(25000),
    ACCELERATE(25001),
    STOP(25002);

    private int value;

    ConeEventType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}

