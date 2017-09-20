package kstn.game.app.network;

import kstn.game.logic.event.EventType;

public enum NetworkTestEventType implements EventType {
    TEST_EVENT1(10100),
    TEST_EVENT2(10101);

    private final int value;

    NetworkTestEventType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
