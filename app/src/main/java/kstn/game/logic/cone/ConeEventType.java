package kstn.game.logic.cone;

import kstn.game.logic.event.EventType;

public enum ConeEventType implements EventType {
    MOVE(27000),
    ACCELERATE(27001),
    STOP(27002);

    private int value;

    ConeEventType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}

