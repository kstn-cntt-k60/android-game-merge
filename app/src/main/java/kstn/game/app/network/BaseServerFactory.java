package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.view.network.Server;
import kstn.game.view.network.ServerFactory;

import java.io.IOException;
import java.util.Map;

public class BaseServerFactory implements ServerFactory {
    private final LLBaseEventManager llEventManager;

    public BaseServerFactory(LLBaseEventManager llEventManager) {
        this.llEventManager = llEventManager;
    }

    @Override
    public Server create(int port, Map<EventType, EventData.Parser> parserMap) throws IOException
    {
        return new BaseServer(port, llEventManager, parserMap);
    }
}
