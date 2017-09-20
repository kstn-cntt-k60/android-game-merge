package kstn.game.app.event;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class BaseEventManager implements EventManager {
	private Map<EventType, List<EventListener>> eventListeners;
	private LinkedList<EventData> eventQueue;
	
	public BaseEventManager() {
		eventListeners = new HashMap<>();
		eventQueue = new LinkedList<EventData>();
	}

	@Override
	public void addListener(EventType type, EventListener listener) {
		List<EventListener> list = eventListeners.get(type);
		if (list == null) {
			list = new ArrayList<>();
			eventListeners.put(type, list);
		}
		list.add(listener);
	}

	@Override
	public void removeListener(EventType type, EventListener listener) {
		List<EventListener> list = eventListeners.get(type);
		if (list != null) {
			list.remove(listener);
		}
	}

	@Override
	public void trigger(EventData event) {
		List<EventListener> list = eventListeners.get(event.getEventType());
		if (list == null)
			return;
		
		for (EventListener listener: list) {
			listener.onEvent(event);
		}
	}

	@Override
	public void queue(EventData event) {
		eventQueue.add(event);
	}

	public void update() {
		while (!eventQueue.isEmpty()) {
			EventData event = eventQueue.poll();
			List<EventListener> list = eventListeners.get(event.getEventType());
			if (list == null)
				continue;
			for (EventListener listener: list)
				listener.onEvent(event);
		}
	}

	@Override
	public void abortEvent(EventType type) {
		ListIterator<EventData> it = eventQueue.listIterator();
		while (it.hasNext()) {
			EventData event = it.next();
			if (event.getEventType() == type) 
				it.remove();
		}
		
	}

	public void abortAllEvents() {
		eventQueue.clear();
	}
	
}
