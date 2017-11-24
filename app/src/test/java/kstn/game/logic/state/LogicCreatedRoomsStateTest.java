package kstn.game.logic.state;

import org.junit.Test;

import java.io.IOException;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.process.BaseProcessManager;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.UDPManager;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicCreatedRoomsStateTest {
    private LogicCreatedRoomsState state;
    private EventManager eventManager = new BaseEventManager();
    private ViewManager root = mock(ViewManager.class);
    private View view = mock(View.class);

    private final WifiInfo wifiInfo = mock(WifiInfo.class);
    private final UDPManagerFactory factory = mock(UDPManagerFactory.class);
    private final ProcessManager processManager;
    private final UDPManager udpManager = mock(UDPManager.class);

    private final int hostIP = 3344;
    private final int port = 2017;
    private final int mask = 888;

    public LogicCreatedRoomsStateTest() {
        processManager = new BaseProcessManager();
        state = new LogicCreatedRoomsState(eventManager,
                root, view, wifiInfo, factory, processManager);


        when(wifiInfo.getIP()).thenReturn(hostIP);
        when(wifiInfo.getMask()).thenReturn(mask);
    }

    @Test
    public void shouldAddViewOnEntryAndRemoveOnExit() {
        verify(root, times(0)).addView(view);
        state.entry();
        verify(root, times(1)).addView(view);
        verify(root, times(0)).removeView(view);
        state.exit();
        verify(root, times(1)).addView(view);
        verify(root, times(1)).removeView(view);
    }

    @Test
    public void shouldCallCreateUDPManagerOnEntry() throws IOException {
        when(factory.create(hostIP, port, mask, null)).thenReturn(udpManager);
        state.entry();
        verify(factory, times(1)).create(hostIP, port, mask, null);
    }

    @Test
    public void shouldCallShutdownUDPManagerOnEntry() throws IOException {
        when(factory.create(hostIP, port, mask, null)).thenReturn(udpManager);
        state.entry();
        state.exit();
        verify(udpManager, times(1)).shutdown();
    }
}