package kstn.game.view.cone;

import java.util.HashMap;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 08/11/2017.
 */

public class ConeStopEventData extends BaseEventData {
    protected int result;
    public ConeStopEventData(int result) {
        super(0);
        this.result = result;
    }

    int getResult() {
        return result;
    }

    @Override
    public EventType getEventType() {
        return ConeEventType.STOP;
    }

    @Override
    public String getName() {
        return "";
    }
}
