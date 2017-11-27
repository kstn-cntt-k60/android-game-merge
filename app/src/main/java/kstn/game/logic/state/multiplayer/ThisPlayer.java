package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.SetThisPlayerEvent;
import kstn.game.logic.state.IEntryExit;

public class ThisPlayer implements IEntryExit {
    private final EventManager eventManager;
    private String name;
    private int avatarId;
    private EventListener setThisPlayerListener;

    public ThisPlayer(EventManager eventManager) {
        this.eventManager = eventManager;
        name = "";
        avatarId = 0;

        setThisPlayerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SetThisPlayerEvent event1 = (SetThisPlayerEvent) event;
                ThisPlayer.this.name = event1.getName();
                ThisPlayer.this.avatarId = event1.getAvatarId();
            }
        };
    }

    public String getName() {
        return name;
    }

    public int getAvatarId() {
        return avatarId;
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SET_THIS_PLAYER, setThisPlayerListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.SET_THIS_PLAYER, setThisPlayerListener);
    }
}
