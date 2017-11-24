package kstn.game.logic.state;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import kstn.game.app.process.BaseProcessManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ViewPlayingStateTestOnUDPEvent {
    private SawCreatedRoomEvent event =
            new SawCreatedRoomEvent(3344, "ABC", 2);
    private SawCreatedRoomEvent event2 =
            new SawCreatedRoomEvent(3344, "ABC", 3);

    private EventManager eventManager = mock(EventManager.class);
    private BaseProcessManager processManager = new BaseProcessManager();
    private final LogicCreatedRoomsState state;

    public ViewPlayingStateTestOnUDPEvent() {
        state = new LogicCreatedRoomsState(
                eventManager, null,
                null, null, null, processManager);
    }

    @Test
    public void onUDPEventShouldSendEventToViewAndCreateProcessToWait() {
        state.onUDPEvent(event);
        verify(eventManager, times(1)).trigger(event);

        clearInvocations(eventManager);
        processManager.updateProcesses(3000);
        verify(eventManager, times(0)).trigger(any(EventData.class));

        clearInvocations(eventManager);
        processManager.updateProcesses(2001);

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager, times(1)).trigger(captor.capture());
        RemoveCreatedRoomEvent removeEvent = (RemoveCreatedRoomEvent) captor.getValue();
        Assert.assertEquals(removeEvent.getIpAddress(), 3344);
    }

    @Test
    public void onUDPEventShouldResetExpireProcessWhenReceiveEvent() {
        state.onUDPEvent(event);
        verify(eventManager, times(1)).trigger(event);

        clearInvocations(eventManager);
        processManager.updateProcesses(3000);
        verify(eventManager, times(0)).trigger(any(EventData.class));

        clearInvocations(eventManager);
        state.onUDPEvent(event);
        verify(eventManager, times(1)).trigger(event);

        clearInvocations(eventManager);
        processManager.updateProcesses(3000);
        verify(eventManager, times(0)).trigger(any(EventData.class));

        clearInvocations(eventManager);
        processManager.updateProcesses(2001);

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager, times(1)).trigger(captor.capture());
        RemoveCreatedRoomEvent removeEvent = (RemoveCreatedRoomEvent) captor.getValue();
        Assert.assertEquals(removeEvent.getIpAddress(), 3344);
        Assert.assertEquals(state.expireProcessMap.size(), 0);
    }
}
