package kstn.game.logic.event;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import kstn.game.app.network.TestMessage;

import static org.junit.Assert.*;

public class TestParser {
	
	private static class TestEventData extends GameEventData {
		private int id;
		private String name;

		public TestEventData(int id, String name, long timeStamp) {
			super(TestEventType.TEST_PARSER);
			this.id = id;
			this.name = name;
		}

		@Override
		public void serialize(OutputStream out) throws IOException {
			TestMessage.Test message = TestMessage.Test.newBuilder()
					.setId(id)
					.setName(name)
					.build();
			
			message.writeTo(out);
		}
		
		public static class Parser implements EventData.Parser {
			@Override
			public EventData parseFrom(InputStream in) throws IOException {
				TestMessage.Test message = null;
				message = TestMessage.Test.parseFrom(in);
				TestEventData data = new TestEventData(message.getId(), message.getName(), 100);
				return data;
			}
		}

	}
	
	@Test
	public void parseFrom() throws Exception {

		FileOutputStream out = new FileOutputStream("test_parser");
		DataOutputStream dataOut = new DataOutputStream(out);

		TestEventData event = new TestEventData(123, "Ta Bao Thang", 1234);
		assertNotEquals(event.getEventType().hashCode(), TestEventType.EVENT_TEST1);
		
		dataOut.writeInt(event.getEventType().getValue());
		event.serialize(out);
		out.close();
		

		FileInputStream in = new FileInputStream("test_parser");
		DataInputStream dataIn = new DataInputStream(in);
		int type =  dataIn.readInt();
		TestEventData getEvent = 
				(TestEventData) new TestEventData.Parser().parseFrom(in);
		assertEquals(getEvent.getEventType().getValue(), type);
		assertEquals(getEvent.id, 123);
		assertEquals(getEvent.name, "Ta Bao Thang");
		in.close();
	}

}
