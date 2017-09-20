package kstn.game.app.network.events;

import kstn.game.app.event.LLBaseEventData;
import kstn.game.app.event.LLBaseEventType;

public class TCPServerAcceptError extends LLBaseEventData {

    public TCPServerAcceptError() {
        super(LLBaseEventType.TCP_SERVER_ACCEPT_ERROR);
    }

}
