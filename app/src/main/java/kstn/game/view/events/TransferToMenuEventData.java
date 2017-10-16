package kstn.game.view.events;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 16/10/2017.
 */

public class TransferToMenuEventData extends BaseEventData {

    public TransferToMenuEventData() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return UIEventType.TRANSFER_TO_MENU;
    }

    @Override
    public String getName() {
        return "";
    }
}