package kstn.game.logic.cone;

import kstn.game.logic.event.GameEventData;

public class ConeStopEventData extends GameEventData {
    protected int result;
    public ConeStopEventData(int result) {
        super(ConeEventType.STOP);
        this.result = result;
    }

    public int getResult() {
        return result;
    }

}
