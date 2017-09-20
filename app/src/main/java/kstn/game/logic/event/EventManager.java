package kstn.game.logic.event;

public interface EventManager {
	
	void addListener(EventType type, EventListener listener);
	
	void removeListener(EventType type, EventListener listener);
	
	void trigger(EventData event);
	
	void queue(EventData event);
	
	void abortEvent(EventType type);
}
