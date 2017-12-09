package kstn.game.view.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.view.thang.model.Player;

public class GameResultInfo {
    List<Player> playerList = new ArrayList<>();
    private int topPlayerIndex;

    public void setTopPlayerIndex(int topPlayerIndex) {
       this.topPlayerIndex = topPlayerIndex;
    }

    public int getTopPlayerIndex() {
        return topPlayerIndex;
    }

    public void clear() {
        playerList.clear();
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
