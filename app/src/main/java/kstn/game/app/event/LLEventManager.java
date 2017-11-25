package kstn.game.app.event;

public interface LLEventManager {

    void addListener(LLEventType eventType, LLListener listener);

    void removeListener(LLEventType eventType, LLListener listener);

    public void queue(LLEventData event);
}
