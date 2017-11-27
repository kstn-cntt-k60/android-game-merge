package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.player.SetThisPlayerEvent;

public class ThisPlayerTest {
    private EventManager eventManager;
    private ThisPlayer thisPlayer;

    public ThisPlayerTest() {
        eventManager = new BaseEventManager();
        thisPlayer = new ThisPlayer(eventManager);
        thisPlayer.entry();
    }

    @Test
    public void shouldUpdateNameBeforeExit() {
        Assert.assertEquals(thisPlayer.getName(), "");
        eventManager.trigger(new SetThisPlayerEvent("AXA", 223));
        Assert.assertEquals(thisPlayer.getName(), "AXA");
    }

    @Test
    public void shouldUpdateAvatarIdBeforeExit() {
        Assert.assertEquals(thisPlayer.getAvatarId(), 0);
        eventManager.trigger(new SetThisPlayerEvent("AXA", 223));
        Assert.assertEquals(thisPlayer.getAvatarId(), 223);
    }

    @Test
    public void shouldNotUpdateAvatarIdAfterExit() {
        eventManager.trigger(new SetThisPlayerEvent("AXA", 223));
        thisPlayer.exit();
        eventManager.trigger(new SetThisPlayerEvent("Tung", 23));
        Assert.assertEquals(thisPlayer.getAvatarId(), 223);
    }

    @Test
    public void shouldNotUpdateNameAfterExit() {
        eventManager.trigger(new SetThisPlayerEvent("AXA", 223));
        thisPlayer.exit();
        eventManager.trigger(new SetThisPlayerEvent("BCC", 55));
        Assert.assertEquals(thisPlayer.getName(), "AXA");
    }

}
