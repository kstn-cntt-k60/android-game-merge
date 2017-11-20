package kstn.game.logic.network;

import java.util.Optional;

public interface WifiInfo {

    boolean isConnected();

    // zero if not connected
    int getIP();

    // zero if not connected
    int getMask();
}
