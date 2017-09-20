package kstn.game.logic.event;

import java.io.IOException;
import java.io.OutputStream;

public interface Serializable {

	void serialize(OutputStream out) throws IOException;
	
}
