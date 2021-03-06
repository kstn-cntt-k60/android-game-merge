package kstn.game.logic.playing_event;

import kstn.game.logic.event.EventType;

public enum PlayingEventType implements EventType {
    MULTI_GAME_OVER(1998),
    GAME_OVER(1997),
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
    CONE_RESULT(2304),
    NEXT_QUESTION(2323),
    NEXT_LEVEL(2324),
    BONUS(2355),

    REQUEST_JOIN_ROOM(2400),
    ACCEPT_JOIN_ROOM(2401),
    START_PLAYING(2402),

    PLAYER_SET_SCORE(17300),
    SET_NUMBER_PLAYER(17301),
    PlAYER_SET_AVATAR(17302),
    PLAYER_SET_NAME(17303),
    PLAYER_DEACTIVATE(17304),
    PLAYER_ACTIVATE(18304),
    NEXT_PLAYER(17305),
    SET_THIS_PLAYER(17306),
    PLAYER_READY(17307),

    SAW_CREATED_ROOM(17400),
    REMOVE_CREATED_ROOM(17401),
    SET_THIS_ROOM(17402),
    CLICK_ROOM(17403),
    EXIT_ROOM(17404),
    SET_ROOM_PLAYER_LIST(17405),

    SHOW_TOAST(17406),
    SONG_FAIL(17407),
    SONG_TINGTING(17408),
    ;

    PlayingEventType(int value) {
        this.value = value;
    }

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
