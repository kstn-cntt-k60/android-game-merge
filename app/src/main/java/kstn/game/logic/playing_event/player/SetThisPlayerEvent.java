package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class SetThisPlayerEvent extends GameEventData {
    private final String name;
    private final int avatarId;

    public SetThisPlayerEvent(String name, int avatarId) {
        super(PlayingEventType.SET_THIS_PLAYER);
        this.name = name;
        this.avatarId = avatarId;
    }

    public String getName() {
        return name;
    }

    public int getAvatarId() {
        return avatarId;
    }
}
