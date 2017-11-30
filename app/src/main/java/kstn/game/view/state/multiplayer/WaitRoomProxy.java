package kstn.game.view.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state.multiplayer.Player;

public class WaitRoomProxy implements IEntryExit {
    private final EventManager eventManager;

    private final EventListener acceptJoinRoomListener;
    private final EventListener exitRoomListener;

    public WaitRoomProxy(EventManager eventManager,
                         final IWaitRoom waitRoom) {
        this.eventManager = eventManager;

        acceptJoinRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                AcceptJoinRoomEvent event1 = (AcceptJoinRoomEvent) event;
                waitRoom.addPlayer(event1.getNewPlayer());
                for (Player player: event1.getOldPlayers()) {
                    waitRoom.addPlayer(player);
                }
            }
        };

        exitRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ExitRoomEvent exitRoomEvent = (ExitRoomEvent) event;
                waitRoom.removePlayer(exitRoomEvent.getPlayerIpAddress());
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
        eventManager.addListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
        eventManager.removeListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptJoinRoomListener);
    }
}
