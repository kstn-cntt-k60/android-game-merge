package kstn.game.app.event;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.TestEventType;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestBaseEventManager {
	EventListener listener1 = mock(EventListener.class);
	EventListener listener2 = mock(EventListener.class);
	EventListener listener3 = mock(EventListener.class);
	
	EventData event1 = mock(EventData.class);
	EventData event2 = mock(EventData.class);
	EventData event3 = mock(EventData.class);

	BaseEventManager manager = new BaseEventManager();
	
	public TestBaseEventManager() {
		manager.addListener(TestEventType.EVENT_TEST1, listener1);
		manager.addListener(TestEventType.EVENT_TEST1, listener2);
		manager.addListener(TestEventType.EVENT_TEST2, listener3);
		
		when(event1.getEventType()).thenReturn(TestEventType.EVENT_TEST1);
		when(event2.getEventType()).thenReturn(TestEventType.EVENT_TEST2);
		when(event3.getEventType()).thenReturn(TestEventType.EVENT_TEST3);
	}
	
	@Test
	public void trigger() {
		manager.trigger(event1);
		verify(listener1, times(1)).onEvent(event1);
		verify(listener2, times(1)).onEvent(event1);
		verify(listener3, times(0)).onEvent(event1);
	}
	
	@Test
	public void queue_and_update() {
		manager.queue(event1);
		manager.queue(event2);

		verify(listener1, times(0)).onEvent(event1);
		verify(listener2, times(0)).onEvent(event1);
		verify(listener3, times(0)).onEvent(event1);

		manager.update();
		
		verify(listener1, times(1)).onEvent(event1);
		verify(listener2, times(1)).onEvent(event1);
		verify(listener3, times(1)).onEvent(event2);
	}
	
	@Test
	public void queue_and_abort() {
		manager.queue(event1);
		manager.queue(event2);
		
		manager.abortEvent(TestEventType.EVENT_TEST1);
		manager.update();
		
		verify(listener1, times(0)).onEvent(event1);
		verify(listener2, times(0)).onEvent(event1);
		verify(listener3, times(1)).onEvent(event2);
	}
	
	@Test
	public void queue_not_have_listener() {
		manager.queue(event3);
		manager.update();
		verify(listener1, times(0)).onEvent(event1);
		verify(listener2, times(0)).onEvent(event1);
		verify(listener3, times(0)).onEvent(event2);
	}

	@Test
	public void testQueuePollAndAdd() {
	    Queue<Integer> queue = new ConcurrentLinkedQueue<>();
	    List<Integer> list = new LinkedList<Integer>();
	    list.add(3);
	    list.add(4);
	    list.add(5);
	    queue.add(1);
	    queue.add(2);
	    int i = 0;
	    int count = 0;
	    while (!queue.isEmpty()) {
	        queue.poll();
	        if (i < 3)
                queue.add(list.get(i++));
	        count++;
        }
        assertEquals(count, 5);
    }
}
