package kstn.game.view.network;

import kstn.game.logic.event.EventData;

public interface UDPManager {

    void shutdown();

    void setReceiveDataListener(OnReceiveDataListener listener);

    void broadcast(EventData event);

    interface OnReceiveDataListener {
        void onReceiveData(EventData event);
    }

}
