package kstn.game.app.network.events;

import kstn.game.app.event.LLBaseEventType;
import kstn.game.logic.event.EventData;
import kstn.game.app.event.LLBaseEventData;

public class TCPReceiveData extends LLBaseEventData {
    private final EventData event;

    public TCPReceiveData(EventData event) {
        super(LLBaseEventType.TCP_RECEIVE_DATA);
        this.event = event;
    }

    public EventData getEvent() {
        return event;
    }
}
