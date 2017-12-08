package kstn.game.view.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerActivateEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerSetAvatarEvent;
import kstn.game.logic.playing_event.player.PlayerSetNameEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;
import kstn.game.logic.playing_event.player.SetNumberPlayerEvent;
import kstn.game.logic.state.IEntryExit;

public class PlayerProxy implements IEntryExit {
    private final IPlayerManager playerManager;
    private final EventManager eventManager;

    private EventListener activateListener;
    private EventListener deactivateListener;
    private EventListener setNameListener;
    private EventListener setAvatarListener;
    private EventListener setScoreListener;
    private EventListener setNumberPlayerListener;
    private EventListener nextPlayerListener;

    public PlayerProxy(EventManager eventManager,
                       IPlayerManager manager) {
        this.playerManager = manager;
        this.eventManager = eventManager;

        setNumberPlayerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SetNumberPlayerEvent event1 = (SetNumberPlayerEvent)event;
                playerManager.setNumberPlayer(event1.getPlayerCount());
            }
        };

        setAvatarListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerSetAvatarEvent event1 = (PlayerSetAvatarEvent)event;
                playerManager.setAvatar(event1.getPlayerIndex(), event1.getAvatarId());
            }
        };

        setNameListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerSetNameEvent event1 = (PlayerSetNameEvent) event;
                playerManager.setName(event1.getPlayerIndex(), event1.getPlayerName());
            }
        };

        setScoreListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerSetScoreEvent event1 = (PlayerSetScoreEvent) event;
                playerManager.setScore(event1.getScore());
            }
        };

        nextPlayerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                NextPlayerEvent event1 = (NextPlayerEvent) event;
                playerManager.nextPlayer(event1.getPlayerIndex());
            }
        };

        activateListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerActivateEvent activateEvent = (PlayerActivateEvent) event;
                playerManager.activatePlayer(activateEvent.getPlayerIndex());
            }
        };

        deactivateListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerDeactivateEvent event1 = (PlayerDeactivateEvent) event;
                playerManager.deactivatePlayer(event1.getPlayerIndex());
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SET_NUMBER_PLAYER, setNumberPlayerListener);
        eventManager.addListener(PlayingEventType.PlAYER_SET_AVATAR, setAvatarListener);
        eventManager.addListener(PlayingEventType.PLAYER_SET_NAME, setNameListener);
        eventManager.addListener(PlayingEventType.PLAYER_ACTIVATE, activateListener);
        eventManager.addListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);
        eventManager.addListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
        eventManager.addListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.removeListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
        eventManager.removeListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);
        eventManager.removeListener(PlayingEventType.PLAYER_ACTIVATE, activateListener);
        eventManager.removeListener(PlayingEventType.PLAYER_SET_NAME, setNameListener);
        eventManager.removeListener(PlayingEventType.PlAYER_SET_AVATAR, setAvatarListener);
        eventManager.removeListener(PlayingEventType.SET_NUMBER_PLAYER, setNumberPlayerListener);
    }
}
