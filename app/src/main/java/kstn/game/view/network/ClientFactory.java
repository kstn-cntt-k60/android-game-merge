package kstn.game.view.network;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;

import java.io.IOException;
import java.util.Map;

public interface ClientFactory {
    Endpoint connect(int serverIP, int serverPort,
                     Map<EventType, EventData.Parser> parserMap)
            throws IOException;
}
