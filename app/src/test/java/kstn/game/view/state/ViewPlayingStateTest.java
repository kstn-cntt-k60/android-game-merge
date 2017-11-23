package kstn.game.view.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state.multiplayer.IThisPlayer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ViewPlayingStateTest {
    private ViewPlayingState state;
    private EventManager eventManager = mock(EventManager.class);
    private IThisPlayer thisPlayer = mock(IThisPlayer.class);

    private IEntryExit entry1 = mock(IEntryExit.class);
    private IEntryExit entry2 = mock(IEntryExit.class);

    public ViewPlayingStateTest() {
        state = new ViewPlayingState(eventManager, thisPlayer);
        state.addEntryExit(entry1);
        state.addEntryExit(entry2);
    }

    @Test
    public void shouldCallEntryMethodsAndNotExitMethodBeforeExit() {
        state.entry();
        verify(entry1, times(1)).entry();
        verify(entry2, times(1)).entry();
        verify(entry1, times(0)).exit();
        verify(entry2, times(0)).exit();
    }

    @Test
    public void shouldCallExitMethodsAfterExit() {
        state.entry();
        state.exit();
        verify(entry1, times(1)).exit();
        verify(entry2, times(1)).exit();
    }

    @Test
    public void shouldTransiteToCreatedRoomsStateWhenPressBack() {
        when(thisPlayer.getAvatarId()).thenReturn(34);
        when(thisPlayer.getName()).thenReturn("Tung");
        state.entry();
        state.onBack();
        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager, times(1)).queue(eventCaptor.capture());
        TransitToCreatedRoomsState event = (TransitToCreatedRoomsState) eventCaptor.getValue();
        Assert.assertEquals(event.getAvatarId(), 34);
        Assert.assertEquals(event.getPlayerName(), "Tung");
    }
}
