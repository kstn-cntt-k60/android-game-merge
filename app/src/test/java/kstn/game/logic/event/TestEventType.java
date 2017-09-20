package kstn.game.logic.event;

import kstn.game.logic.event.EventType;

public enum TestEventType implements EventType {
    EVENT_TEST1(10000),
    EVENT_TEST2(10001),
    EVENT_TEST3(10002),
    TEST_PARSER(10003);

    private final int value;

    private TestEventType(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
}
