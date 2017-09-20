package kstn.game.view.network;

public interface Server {
    Endpoint getEndpoint();

    void setAcceptErrorListener(OnAcceptErrorListener listener);

    void shutdown();

    interface OnAcceptErrorListener {
        void onAcceptError();
    }
}
