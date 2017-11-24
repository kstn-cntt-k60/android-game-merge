package kstn.game.app.event;

import org.junit.Test;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestLLBaseEventManager {
    private LLBaseEventManager manager = null;
    LLListener listener1 = mock(LLListener.class);
    LLListener listener2 = mock(LLListener.class);
    LLListener listener3 = mock(LLListener.class);
    LLListener listener4 = mock(LLListener.class);

    LLEventData event1 = new LLBaseEventData(LLTestEventType.TEST_EVENT1);
    LLEventData event2 = new LLBaseEventData(LLTestEventType.TEST_EVENT2);
    LLEventData event3 = new LLBaseEventData(LLTestEventType.TEST_EVENT3);
    LLEventData event4 = new LLBaseEventData(LLTestEventType.TEST_EVENT4);

    public TestLLBaseEventManager() {
        manager = new LLBaseEventManager();

        manager.addListener(LLTestEventType.TEST_EVENT1, listener1);
        manager.addListener(LLTestEventType.TEST_EVENT1, listener2);
        manager.addListener(LLTestEventType.TEST_EVENT2, listener3);
        manager.addListener(LLTestEventType.TEST_EVENT3, listener4);
    }


    @Test
    public void test() {
        assertNotEquals(event1.getEventType(), event2.getEventType());
        assertNotEquals(event2.getEventType(), event3.getEventType());
        assertEquals(event1.getEventType(), LLTestEventType.TEST_EVENT1);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                manager.queue(event1);
                manager.queue(event2);
                manager.queue(event3);
                manager.queue(event4);
            }
        });
        thread.run();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        InOrder order = inOrder(listener1, listener2, listener3, listener4);
        InOrder order2 = inOrder(listener3, listener1);

        manager.update();
        manager.update();
        manager.update();

        order.verify(listener1, times(1)).onEvent(event1);
        order.verify(listener2, times(1)).onEvent(event1);
        order.verify(listener3, times(1)).onEvent(event2);
        order.verify(listener4, times(0)).onEvent(null);
    }

}
