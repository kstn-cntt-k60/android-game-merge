package kstn.game.app.network;

import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.LLBaseEventType;
import kstn.game.app.event.LLEventData;
import kstn.game.app.event.LLListener;
import kstn.game.app.network.events.TCPServerAcceptError;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class BaseServer implements Server, Runnable {
    private final int backlog = 4;

    private final BaseEndpoint endpoint;
    private final LLBaseEventManager llEventManager;
    private ServerSocket serverSocket = null;
    private Thread thread = null;
    private volatile boolean running = false;

    private OnAcceptErrorListener acceptErrorListener = null;

    BaseServer(int port, LLBaseEventManager llEventManager,
               Map<EventType, EventData.Parser> parserMap)
            throws IOException {
        this.llEventManager = llEventManager;

        llEventManager.addListener(LLBaseEventType.TCP_SERVER_ACCEPT_ERROR,
                new LLListener() {
                    @Override
                    public void onEvent(LLEventData event) {
                        if (acceptErrorListener != null) {
                            acceptErrorListener.onAcceptError();
                        }
                    }
                });

        this.endpoint = new BaseEndpoint(llEventManager, parserMap);
        serverSocket = new ServerSocket(port, backlog);
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public Endpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public void setAcceptErrorListener(OnAcceptErrorListener listener) {
        this.acceptErrorListener = listener;
    }

    @Override
    public void shutdown() {
        this.running = false;
        if (serverSocket == null)
            return;
        try {
            serverSocket.close();
        } catch (IOException e) {
        }

        try {
            if (thread != null)
                thread.join();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                this.endpoint.addConnection(socket);
            } catch (IOException e) {
                llEventManager.queue(new TCPServerAcceptError());
                running = false;
                return;
            }
        }
    }
}
