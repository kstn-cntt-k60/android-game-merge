package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.TestEventType;
import kstn.game.view.network.UDPManager;
import kstn.game.view.network.UDPManagerFactory;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TestUDPServerClient {
	private boolean running = true;
	private TestMessage.Test msg;
	private String hostIP = "10.10.105.179";
	private LLBaseEventManager llBaseEventManager = new LLBaseEventManager();
	private UDPManagerFactory managerFactory = new UDPBaseManagerFactory(llBaseEventManager);
	private Map<EventType, EventData.Parser> parserMap = new HashMap<>();
	private UDPManager.OnReceiveDataListener receiveListener
			= mock(UDPManager.OnReceiveDataListener.class);

	public TestUDPServerClient() {
		parserMap.put(TestEventType.EVENT_TEST1, new TestEventData1.Parser());
	}

	@Test
	public void test() throws Exception {
		UDPBaseManager server = (UDPBaseManager) managerFactory.create(
				NetworkUtil.ipStringToInt(this.hostIP),
				2013, 0xFFFF0000, parserMap);
        server.setReceiveDataListener(receiveListener);

		UDPBaseManager client = (UDPBaseManager) managerFactory.create(
				NetworkUtil.ipStringToInt(this.hostIP),
				2014,0xFFFF0000, parserMap
		);

		client.sendPort = 2013;

		TestEventData1 event1 = new TestEventData1(133, 100, "Ta Quang Tung");
		TestEventData1 event2 = new TestEventData1(144, 200, "Van");

		client.broadcast(event1);
		client.broadcast(event2);

		Thread.currentThread().sleep(50);

		llBaseEventManager.update();

		ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);

		verify(receiveListener, times(2)).onReceiveData(captor.capture());
		List<EventData> receiveEvents = captor.getAllValues();

		TestEventData1 receiveEvent1 = (TestEventData1) receiveEvents.get(0);
		assertEquals(receiveEvent1.id(), event1.id());
		assertEquals(receiveEvent1.name(), event1.name());

		TestEventData1 receiveEvent2 = (TestEventData1) receiveEvents.get(1);
		assertEquals(receiveEvent2.id(), event2.id());
		assertEquals(receiveEvent2.name(), event2.name());

		client.shutdown();
		server.shutdown();
	}

}
