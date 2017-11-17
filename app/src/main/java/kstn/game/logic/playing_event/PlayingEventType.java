package kstn.game.logic.playing_event;

import kstn.game.logic.event.EventType;

public enum PlayingEventType implements EventType {
    VIEW_SINGLE_PLAYER_READY(2299),
    LOGIC_SINGLE_PLAYER_READY(2297),
    PLAYING_READY(2296),

    OPEN_MULTIPLE_CELL(2399),
    GIVE_CHOOSE_CELL(2356),
    CELL_CHOSEN(2357),
    OPEN_CELL(2358),

    GUESS_RESULT(2133),
    REQUEST_GUESS(2134),
    CANCEL_GUESS(2135),
    ACCEPT_REQUEST_GUESS(2136),

    GIVE_ANSWER(2300),
    ANSWER(2301),
    PLAYER_STATE_CHANGE(2398),
    NEXT_PLAYER(2302),
    NEXT_LEVEL(2303),
    ROTATE_RESULT(2304),
    NEXT_QUESTION(2323),
    BONUS(2355),
    OUT_OF_LIFE(2322),

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
