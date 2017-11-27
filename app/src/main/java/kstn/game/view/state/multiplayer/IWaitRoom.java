package kstn.game.view.state.multiplayer;

import kstn.game.logic.state.multiplayer.Player;

public interface IWaitRoom {

    void addPlayer(Player player);

    void removePlayer(int playerIpAddress);

}
