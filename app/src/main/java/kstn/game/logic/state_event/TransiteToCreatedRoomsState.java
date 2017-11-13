package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.model.PlayerModel;

/**
 * Created by qi on 13/11/2017.
 */

public class TransiteToCreatedRoomsState extends BaseEventData {
    public final PlayerModel player;

    public TransiteToCreatedRoomsState(PlayerModel player) {
        super(0);
        this.player = player;
    }

    @Override
    public EventType getEventType() {
        return StateEventType.CREATED_ROOMS;
    }

    @Override
    public String getName() {
        return null;
    }
}
