package kstn.game.view.network;

import kstn.game.logic.event.EventData;

public interface Endpoint {

    void send(EventData event);

    void setConnectionErrorListener(OnConnectionErrorListener listener);

    void setReceiveDataListener(OnReceiveDataListener listener);

    void shutdown();

    interface OnConnectionErrorListener {
        void onConnectionError(Connection connection);
    }

    interface OnReceiveDataListener {
        void onReceiveData(EventData event);
    }
}
