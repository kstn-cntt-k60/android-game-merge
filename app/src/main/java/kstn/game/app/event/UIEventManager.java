package kstn.game.app.event;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;

// Working on UI Thread
public class UIEventManager implements EventManager {
    private final Map<EventListener, EventListener> uiLogicListenerMap = new HashMap<>();
    private final Activity activity;
    private final LLEventManager llEventManager;
    private final EventManager baseEventManager;

    public UIEventManager(Activity activity, LLEventManager llEventManager,
                          EventManager baseEventManager) {
        this.activity = activity;
        this.llEventManager = llEventManager;
        this.baseEventManager = baseEventManager;
        methodsRunOnLogicThread();
    }

    private void methodsRunOnLogicThread() {
        llEventManager.addListener(LLBaseEventType.UI_ADD_LISTENER,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        UIAddListener addListener = (UIAddListener) event;
                        baseEventManager.addListener(addListener.getType(),
                                addListener.getListener());
                    }
                });

        llEventManager.addListener(LLBaseEventType.UI_REMOVE_LISTENER,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        UIRemoveListener removeListener = (UIRemoveListener) event;
                        baseEventManager.removeListener(removeListener.getType(),
                                removeListener.getListener());
                    }
                });

        llEventManager.addListener(LLBaseEventType.UI_QUEUE_EVENT,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        UIQueueEvent queueEvent = (UIQueueEvent) event;
                        baseEventManager.trigger(queueEvent.getEvent());
                    }
                });
    }

    private class RunOnUIThread implements Runnable {
        private final EventData event;
        private final EventListener listener;

        private RunOnUIThread(EventData event, EventListener listener) {
            this.event = event;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.onEvent(event);
        }
    }

    @Override
    public void addListener(EventType type, final EventListener listener) {
        EventListener logicListener = new EventListener() {
            @Override
            public void onEvent(final EventData event) {
                activity.runOnUiThread(new RunOnUIThread(event, listener));
            }
        };
        llEventManager.queue(new UIAddListener(type, logicListener));
        uiLogicListenerMap.put(listener, logicListener);
    }

    @Override
    public void removeListener(EventType type, EventListener listener) {
        EventListener logicListener = uiLogicListenerMap.get(listener);
        llEventManager.queue(new UIRemoveListener(type, logicListener));
        uiLogicListenerMap.remove(listener);
    }

    // Forbidden
    @Override
    public void trigger(EventData event) {
        assert (false);
    }

    @Override
    public void queue(EventData event) {
        llEventManager.queue(new UIQueueEvent(event));
    }

    // Not support
    @Override
    public void abortEvent(EventType type) {
        assert (false);
    }
}
