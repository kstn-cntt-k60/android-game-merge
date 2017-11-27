package kstn.game.logic.playing_event.room;

import java.util.List;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.multiplayer.Player;

public class AcceptJoinRoomEvent extends GameEventData {
    private final Player newPlayer;
    private final List<Player> oldPlayers;

    public AcceptJoinRoomEvent(Player newPlayer, List<Player> oldPlayers) {
        super(PlayingEventType.ACCEPT_JOIN_ROOM);
        this.newPlayer = newPlayer;
        this.oldPlayers = oldPlayers;
    }

    public Player getNewPlayer() {
        return newPlayer;
    }

    public List<Player> getOldPlayers() {
        return oldPlayers;
    }
}
