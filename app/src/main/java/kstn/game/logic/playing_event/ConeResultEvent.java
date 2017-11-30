package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class ConeResultEvent extends GameEventData {
    private int result;

    public ConeResultEvent(int result) {
        super(PlayingEventType.ROTATE_RESULT);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
