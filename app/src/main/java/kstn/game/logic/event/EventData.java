package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;

import kstn.game.logic.network.Connection;

public interface EventData extends Serializable {
	EventType getEventType();

	// In millisecond
	long getTimeStamp();

	void setConnection(Connection connection);

	Connection getConnection();
	
	String getName();
	
	interface Parser {
		EventData parseFrom(InputStream in) throws IOException;
	}

}
