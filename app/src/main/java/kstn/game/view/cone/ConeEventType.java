package kstn.game.view.cone;

import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/10/2017.
 */

public enum ConeEventType implements EventType {
    MOVE(5000),
    ACCELERATE(5001),
    STOP(5002);

    private int value;

    ConeEventType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}

