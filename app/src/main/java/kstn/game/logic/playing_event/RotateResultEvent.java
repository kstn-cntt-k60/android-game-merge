package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by tung on 14/11/2017.
 */

public class RotateResultEvent extends BaseEventData {
    private String result;

    public RotateResultEvent(String result) {
        super(0);
        this.result = result;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.ROTATE_RESULT;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String getName() {
        return null;
    }
}
