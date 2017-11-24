package kstn.game.logic.state_event;

import java.util.List;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.state.singleplayer.SinglePlayerModel;

/**
 * Created by qi on 13/11/2017.
 */

public class TransitToPlayingState extends BaseEventData {
    public final List<SinglePlayerModel> playerList;

    public TransitToPlayingState(List<SinglePlayerModel> playerList) {
        super(0);
        this.playerList = playerList;
    }

    @Override
    public EventType getEventType() {
        return StateEventType.PLAYING;
    }

    @Override
    public String getName() {
        return null;
    }
}
