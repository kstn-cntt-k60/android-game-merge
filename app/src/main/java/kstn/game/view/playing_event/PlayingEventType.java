package kstn.game.view.playing_event;

import kstn.game.logic.event.EventType;

/**
 * Created by qi on 14/11/2017.
 */

public enum PlayingEventType implements EventType {
    TYPED_CHARACTER(20),
    NEXT_TURN(21),
    OVER_CELL(22),
    NEXT_QUESTION(23)

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
