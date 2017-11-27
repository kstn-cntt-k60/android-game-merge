package kstn.game.logic.state_event;

import kstn.game.logic.event.EventType;

public enum StateEventType implements EventType {
    MENU(30),
    LOGIN(31),
    CREATED_ROOMS(32),
    ROOM_CREATOR(33),
    WAIT_ROOM(34),
    PLAYING(35),
    RESULT(36),
    SINGLE_PLAYER(37),
    SINGLE_RESULT(38),
    STAT(39);

    StateEventType(int value) {
        this.value = value;
    }

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
