package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.process.BaseProcessManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.network.UDPManager;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicCreatedRoomsStateTest {
    private LogicCreatedRoomsState state;
    private BaseEventManager eventManager = new BaseEventManager();
    private ViewManager root = mock(ViewManager.class);
    private View view = mock(View.class);

    private final UDPForwarder forwarder = mock(UDPForwarder.class);
    private final ProcessManager processManager;

    private final int hostIP = 3344;
    private final int port = 2017;
    private final int mask = 888;

    public LogicCreatedRoomsStateTest() {
        processManager = new BaseProcessManager();
        state = new LogicCreatedRoomsState(eventManager,
                root, view, forwarder, processManager);
    }

    @Test
    public void shouldAddViewOnEntryAndRemoveOnExit() throws IOException {
        verify(root, times(0)).addView(view);
        state.entry();
        verify(root, times(1)).addView(view);
        verify(root, times(0)).removeView(view);
        state.exit();
        verify(root, times(1)).addView(view);
        verify(root, times(1)).removeView(view);
    }

    @Test
    public void shouldCallForwarderListenOnEntry() throws IOException {
        state.entry();
        verify(forwarder, times(1)).listen();
    }

    @Test
    public void shouldCallShutdownForwarderOnExit() {
        state.entry();
        verify(forwarder, times(0)).shutdown();
        state.exit();
        verify(forwarder, times(1)).shutdown();
    }

    @Test
    public void shouldGoBackLoginStateWhenUDPFactoryThrow() throws IOException {
        doThrow(new IOException()).when(forwarder).listen();

        EventListener toLoginListener = mock(EventListener.class);
        eventManager.addListener(StateEventType.LOGIN, toLoginListener);
        state.entry();
        eventManager.update();
        verify(toLoginListener, times(1)).onEvent(any(EventData.class));
    }
}
