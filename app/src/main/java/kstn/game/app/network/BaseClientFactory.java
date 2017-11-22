package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.Endpoint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

public class BaseClientFactory implements ClientFactory {
    private final LLBaseEventManager llEventManager;

    public BaseClientFactory(LLBaseEventManager llEventManager) {
        this.llEventManager = llEventManager;
    }

    @Override
    public Endpoint connect(int serverIP, int serverPort,
                            Map<EventType, EventData.Parser> parserMap)
            throws IOException {
        BaseEndpoint endpoint = new BaseEndpoint(llEventManager, parserMap);
        byte[] tmp = new byte[4];
        NetworkUtil.bigEndianToByte(serverIP, tmp);
        Socket socket = null;
        socket = new Socket(InetAddress.getByAddress(tmp), serverPort);
        endpoint.addConnection(socket);

        return endpoint;
    }
}
