package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseEventData implements EventData {
	// In millisecond
	private final long timeStamp;
	
	public BaseEventData(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public abstract EventType getEventType();

	@Override
	public final long getTimeStamp() { return timeStamp; }

	@Override
	public void serialize(OutputStream out) throws IOException {}

	@Override
	public abstract String getName();
	
	public static abstract class Parser implements EventData.Parser {
		@Override
		public EventData parseFrom(InputStream in) throws IOException { return null; }
	}

}
