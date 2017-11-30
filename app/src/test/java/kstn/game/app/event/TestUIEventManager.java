package kstn.game.app.event;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import kstn.game.app.network.TestEventData1;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.event.TestEventType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TestUIEventManager {
    private LLBaseEventManager llEventManager = new LLBaseEventManager();
    private Activity activity = mock(Activity.class);
    private EventManager baseEventManager = new BaseEventManager();
    private UIEventManager manager = new UIEventManager(activity, llEventManager, baseEventManager);

    private EventListener uiListener1 = mock(EventListener.class);
    private EventListener uiListener2 = mock(EventListener.class);

    private EventListener logicListener1 = mock(EventListener.class);
    private EventListener logicListener2 = mock(EventListener.class);

    private TestEventData1 testEventData1 = new TestEventData1(123, 333, "TUNG");
    private GameEventData testEventData2 = mock(GameEventData.class);
    private TestEventData1 testEventData3 = new TestEventData1(555, 222, "VAN");

    @Before
    public void Constructor() {
        when(testEventData2.getEventType()).thenReturn(TestEventType.EVENT_TEST2);
        when(testEventData2.getName()).thenReturn("TEST");

        baseEventManager.addListener(TestEventType.EVENT_TEST1, logicListener1);
        baseEventManager.addListener(TestEventType.EVENT_TEST2, logicListener2);
    }

    @Test
    public void testQueue() {
        manager.queue(testEventData1);
        manager.queue(testEventData2);
        manager.queue(testEventData3);
        llEventManager.update();

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(logicListener1, times(2)).onEvent(captor.capture());
        List<EventData> eventList = captor.getAllValues();
        assertEquals(eventList.get(0), testEventData1);
        assertEquals(eventList.get(1), testEventData3);
        verify(logicListener2, times(1)).onEvent(testEventData2);
    }

    @Test
    public void testUIListener() {
        manager.addListener(TestEventType.EVENT_TEST1, uiListener1);
        manager.addListener(TestEventType.EVENT_TEST2, uiListener2);

        manager.removeListener(TestEventType.EVENT_TEST2, uiListener2);

        llEventManager.update();

        baseEventManager.trigger(testEventData1);
        baseEventManager.trigger(testEventData2);
        baseEventManager.trigger(testEventData3);

        ArgumentCaptor<Runnable> activityCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(activity, times(2)).runOnUiThread(activityCaptor.capture());
        List<Runnable> runnableList = activityCaptor.getAllValues();
        for (Runnable runnable: runnableList)
            runnable.run();

        // manager.removeListener(TestEventType.EVENT_TEST2, uiListener2);

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(uiListener1, times(2)).onEvent(captor.capture());
        List<EventData> eventList = captor.getAllValues();
        assertEquals(eventList.get(0), testEventData1);
        assertEquals(eventList.get(1), testEventData3);

        verify(uiListener2, times(0)).onEvent((EventData) any());
    }

}
