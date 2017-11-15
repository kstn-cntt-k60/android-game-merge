package kstn.game.logic.playing_event;

import kstn.game.logic.event.EventType;

public enum PlayingEventType implements EventType {
    GIVE_ANSWER(2300),
    ANSWER(2301),
    NEXT_PLAYER(2302),
    NEXT_LEVEL(2303),
    ROTATE_RESULT(2304),
    NEXT_QUESTION(2323),
    OVER_CELL(2322),

    REQUEST_JOIN_ROOM(2400),
    ACCEPT_JOIN_ROOM(2401),
    START_PLAYING(2402),
    ROOM_INFO(2403),
    ROOM_INFO_BROADCAST(2404),
    PLAYER_DISCONNECTED(2405)
    ;
    PlayingEventType(int value) {
        this.value = value;
    }

    private int value;
    @Override
    public int getValue() {
        return 0;
    }
}
