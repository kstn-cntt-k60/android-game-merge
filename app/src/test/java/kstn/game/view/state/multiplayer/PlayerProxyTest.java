package kstn.game.view.state.multiplayer;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerSetAvatarEvent;
import kstn.game.logic.playing_event.player.PlayerSetNameEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;
import kstn.game.logic.playing_event.player.SetNumberPlayerEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PlayerProxyTest {
    private BaseEventManager eventManager;
    private IPlayerManager playerManager = mock(IPlayerManager.class);
    private PlayerProxy proxy;

    public PlayerProxyTest() {
        eventManager = new BaseEventManager();
        proxy = new PlayerProxy(eventManager, playerManager);
        proxy.entry();
    }

    @Test
    public void shouldCallSetNumberPlayerBeforeExit() {
        eventManager.trigger(new SetNumberPlayerEvent(33));
        verify(playerManager, times(1)).setNumberPlayer(33);
    }

    @Test
    public void shouldNotCallSetNumberPlayerAfterExit() {
        proxy.exit();
        eventManager.trigger(new SetNumberPlayerEvent(23));
        verify(playerManager, times(0)).setNumberPlayer(0);
    }

    @Test
    public void shouldCallSetNameBeforeExit() {
        eventManager.trigger(new PlayerSetNameEvent(2, "TUNG"));
        verify(playerManager, times(1)).setName(2, "TUNG");
    }

    @Test
    public void shouldNotCallSetNameAfterExit() {
        proxy.exit();
        eventManager.trigger(new PlayerSetNameEvent(2, "TUNG"));
        verify(playerManager, times(0)).setName(2, "TUNG");
    }

    @Test
    public void shouldCallSetAvatarBeforeExit() {
        eventManager.trigger(new PlayerSetAvatarEvent(2, 5));
        verify(playerManager, times(1)).setAvatar(2, 5);
    }

    @Test
    public void shouldNotCallSetAvatarAfterExit() {
        proxy.exit();
        eventManager.trigger(new PlayerSetAvatarEvent(2, 5));
        verify(playerManager, times(0)).setAvatar(2, 5);
    }

    @Test
    public void shouldCallDeactivateBeforeExit() {
        eventManager.trigger(new PlayerDeactivateEvent(10));
        verify(playerManager, times(1)).deactivatePlayer(10);
    }

    @Test
    public void shouldNotCallDeactivateAfterExit() {
        proxy.exit();
        eventManager.trigger(new PlayerDeactivateEvent(10));
        verify(playerManager, times(0)).deactivatePlayer(10);
    }

    @Test
    public void shouldCallSetScoreBeforeExit() {
        eventManager.trigger(new PlayerSetScoreEvent(22));
        verify(playerManager, times(1)).setScore(22);
    }

    @Test
    public void shouldNotCallSetScoreAfterExit() {
        proxy.exit();
        eventManager.trigger(new PlayerSetScoreEvent(10));
        verify(playerManager, times(0)).setScore(10);
    }

    @Test
    public void shouldCallNextPlayerBeforeExit() {
        eventManager.trigger(new NextPlayerEvent(13));
        verify(playerManager, times(1)).nextPlayer(13);
    }

    @Test
    public void shouldNotCallNextPlayerAfterExit() {
        proxy.exit();
        eventManager.trigger(new NextPlayerEvent(10));
        verify(playerManager, times(0)).nextPlayer(10);
    }
}
