package kstn.game.app.root;

import kstn.game.app.event.LLBaseEventData;
import kstn.game.app.event.LLBaseEventType;

/**
 * Created by tung on 12/09/2017.
 */

public class RootResumeEvent extends LLBaseEventData {
    private final long timeStamp;

    public RootResumeEvent(long timeStamp) {
        super(LLBaseEventType.ROOT_RESUME);
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() { return timeStamp; }
}
