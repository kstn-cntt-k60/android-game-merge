package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.view.network.UDPManager;
import kstn.game.view.network.UDPManagerFactory;

import java.io.IOException;
import java.util.Map;

public class UDPBaseManagerFactory implements UDPManagerFactory {
    private final LLBaseEventManager llEventManager;

    public UDPBaseManagerFactory(LLBaseEventManager llEventManager) {
        this.llEventManager = llEventManager;
    }

    @Override
    public UDPManager create(int hostIP, int port, int mask,
                             Map<EventType, EventData.Parser> parserMap)
            throws IOException
    {
        return new UDPBaseManager(hostIP, port, mask, parserMap, llEventManager);
    }
}
