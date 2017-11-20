package kstn.game.logic.network;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;

import java.io.IOException;
import java.util.Map;

public interface UDPManagerFactory {
    UDPManager create(int hostIP, int port, int mask,
                      Map<EventType, EventData.Parser> parserMap) throws IOException;
}
