package kstn.game.app.network;

import kstn.game.app.event.LLEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Server;
import kstn.game.logic.network.ServerFactory;

import java.io.IOException;
import java.util.Map;

public class BaseServerFactory implements ServerFactory {
    private final LLEventManager llEventManager;

    public BaseServerFactory(LLEventManager llEventManager) {
        this.llEventManager = llEventManager;
    }

    @Override
    public Server create(int port, Map<EventType, EventData.Parser> parserMap) throws IOException
    {
        return new BaseServer(port, llEventManager, parserMap);
    }
}
