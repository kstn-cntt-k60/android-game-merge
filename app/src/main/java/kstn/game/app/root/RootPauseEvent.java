package kstn.game.app.root;

import kstn.game.app.event.LLBaseEventData;
import kstn.game.app.event.LLBaseEventType;

public class RootPauseEvent extends LLBaseEventData {
    private final long timeStamp;

    public RootPauseEvent(long timeStamp) {
        super(LLBaseEventType.ROOT_PAUSE);
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() { return timeStamp; }
}
