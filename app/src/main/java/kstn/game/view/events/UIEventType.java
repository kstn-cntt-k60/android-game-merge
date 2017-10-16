package kstn.game.view.events;

import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/10/2017.
 */

public enum UIEventType implements EventType {
    TRANSFER_TO_PLAY(3000),
    TRANSFER_TO_MENU(3001);

    private int id;

    UIEventType(int id) {
        this.id = id;
    }

    @Override
    public int getValue() {
        return id;
    }

}
