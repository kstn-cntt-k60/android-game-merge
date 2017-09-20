package kstn.game.app.root;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.LLBaseEventType;
import kstn.game.app.event.LLEventData;
import kstn.game.app.event.LLListener;

public class BaseTimeManager {
    private final LLBaseEventManager llEventManager;
    private long timeDifferent = 0;
    private long previousPauseTime = 0;

    public BaseTimeManager(LLBaseEventManager llEventManager) {
        this.llEventManager = llEventManager;
        llEventManager.addListener(LLBaseEventType.ROOT_RESUME,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        onResume((RootResumeEvent) event);
                    }
                });
        llEventManager.addListener(LLBaseEventType.ROOT_PAUSE,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        onPause((RootPauseEvent) event);
                    }
                });
    }

    private void onResume(RootResumeEvent event) {
        if (previousPauseTime == 0)
            return;
        timeDifferent += event.getTimeStamp() - previousPauseTime;
    }

    private void onPause(RootPauseEvent event) {
        previousPauseTime = event.getTimeStamp();
    }

    public long getCurrentMillis() {
        return System.currentTimeMillis() - timeDifferent;
    }
}
