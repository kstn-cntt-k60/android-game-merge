package kstn.game.logic.event;

import org.mockito.ArgumentCaptor;

import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.state.IEntryExit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class EventUtil {
    public static EventManager getEventManager() {
        return new BaseEventManager();
    }

    public static EventManager getMockedEventManager() {
        return mock(EventManager.class);
    }

    public static EventListener getMockedListener(
            EventManager eventManager,
            EventType eventType) {
        EventListener listener = mock(EventListener.class);
        eventManager.addListener(eventType, listener);
        return listener;
    }

    public static void assertAddListener(EventManager eventManager, EventType type) {
        verify(eventManager).addListener(type, any(EventListener.class));
    }

    public static void assertRemoveListener(EventManager eventManager, EventType type) {
        verify(eventManager).removeListener(type, any(EventListener.class));
    }

    public static void update(EventManager eventManager) {
        ((BaseEventManager) eventManager).update();
    }

    public static void assertTriggered(EventListener mockedListener) {
        verify(mockedListener).onEvent(any(EventData.class));
    }

    public static void assertTriggered(
                EventListener mockedListener, int n) {
        verify(mockedListener, times(n)).onEvent(any(EventData.class));
    }

    public static void assertTriggered(
                EventListener mockedListener, EventData eventData) {
        verify(mockedListener).onEvent(eventData);
    }

    public static EventData assertTriggeredReturn(
                EventListener mockedListener) {
        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(mockedListener).onEvent(eventCaptor.capture());
        return eventCaptor.getValue();
    }

    public static void assertNotTriggered(EventListener listener) {
        verify(listener, never()).onEvent(any(EventData.class));
    }

    public static List<EventData> assertTriggeredReturn(
                EventListener mockedListener, int n) {
        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(mockedListener, times(n)).onEvent(eventCaptor.capture());
        return eventCaptor.getAllValues();
    }

    public static void assertNotSetUpListener(
            EventManager eventManager,
            EventType eventType,
            IEntryExit entryExit) {
        entryExit.entry();
        verify(eventManager, never()).addListener(eq(eventType), any(EventListener.class));
        entryExit.exit();
        verify(eventManager, never()).removeListener(eq(eventType), any(EventListener.class));
    }

    public static EventListener assertSetUpListener (
            EventManager mockedManager,
            EventType eventType, IEntryExit entryExit) {
        ArgumentCaptor<EventListener> listenerCaptor = ArgumentCaptor.forClass(EventListener.class);

        entryExit.entry();
        verify(mockedManager).addListener(eq(eventType), listenerCaptor.capture());
        EventListener listener = listenerCaptor.getValue();

        entryExit.exit();
        verify(mockedManager).removeListener(eventType, listener);
        return listener;
    }
}
