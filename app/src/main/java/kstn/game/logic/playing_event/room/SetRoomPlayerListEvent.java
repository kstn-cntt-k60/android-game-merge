package kstn.game.logic.playing_event.room;

import java.util.List;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.multiplayer.Player;

public class SetRoomPlayerListEvent extends GameEventData {
    private final List<Player> playerList;

    public SetRoomPlayerListEvent(List<Player> playerList) {
        super(PlayingEventType.SET_ROOM_PLAYER_LIST);
        this.playerList = playerList;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
