package kstn.game.app.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

// Allow to work in multithread environment
public class LLBaseEventManager implements LLEventManager {
    private ConcurrentLinkedQueue<LLEventData> eventQueue
            = new ConcurrentLinkedQueue<>();
    private Map<LLEventType, List<LLListener>> listenerMap =
            new HashMap<>();

    @Override
    public void addListener(LLEventType eventType, LLListener listener) {
        if (listenerMap.get(eventType) == null)
            listenerMap.put(eventType, new ArrayList<LLListener>());
        List<LLListener> list = listenerMap.get(eventType);
        list.add(listener);
    }

    @Override
    public void removeListener(LLEventType eventType, LLListener listener) {
        List<LLListener> list = listenerMap.get(eventType);
        if (list != null) {
            list.remove(listener);
        }
    }

    // Thread Safe
    @Override
    public void queue(LLEventData event) {
        eventQueue.add(event);
    }

    public void update() {
        while (!eventQueue.isEmpty()) {
            LLEventData event = eventQueue.poll();

            List<LLListener> list = listenerMap.get(event.getEventType());
            if (list == null)
                continue;

            for (LLListener listener: list) {
                listener.onEvent(event);
            }
        }
    }
}
