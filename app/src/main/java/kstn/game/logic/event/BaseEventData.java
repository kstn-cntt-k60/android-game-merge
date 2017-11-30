package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.network.Connection;

public abstract class BaseEventData implements EventData {
	private final long timeStamp;
	private Connection connection;

	public BaseEventData(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public Connection getConnection() {
		return connection;
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
