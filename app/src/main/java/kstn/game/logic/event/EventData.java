package kstn.game.logic.event;

import java.io.IOException;
import java.io.InputStream;

public interface EventData extends Serializable {

	EventType getEventType();

	// In millisecond
	long getTimeStamp();
	
	String getName();
	
	public interface Parser {
		EventData parseFrom(InputStream in) throws IOException;
	}

}
