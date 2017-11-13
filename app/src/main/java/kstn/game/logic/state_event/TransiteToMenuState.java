package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 13/11/2017.
 */

public class TransiteToMenuState extends BaseEventData {
    public TransiteToMenuState() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return StateEventType.MENU;
    }

    @Override
    public String getName() {
        return null;
    }
}
