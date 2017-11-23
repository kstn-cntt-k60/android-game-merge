package kstn.game.logic.state;

import java.io.IOException;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.UDPManager;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicCreatedRoomsState extends LogicGameState {
    private final EventManager eventManager;
    private final ViewManager root;
    private final View backgroundView;

    private final WifiInfo wifiInfo;
    private final UDPManagerFactory udpFactory;
    private UDPManager udpManager;

    public LogicCreatedRoomsState(EventManager eventManager,
                                  ViewManager root,
                                  View backgroundView,
                                  WifiInfo wifiInfo,
                                  UDPManagerFactory udpFactory,
                                  ProcessManager processManager) {
        super(null);
        this.eventManager = eventManager;
        this.root = root;
        this.backgroundView = backgroundView;

        this.wifiInfo = wifiInfo;
        this.udpFactory = udpFactory;
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        try {
            udpManager = udpFactory.create(wifiInfo.getIP(), 2017, wifiInfo.getMask(), null);
        } catch (IOException e) {
        }
    }

    @Override
    public void exit() {
        root.removeView(backgroundView);
    }
}
