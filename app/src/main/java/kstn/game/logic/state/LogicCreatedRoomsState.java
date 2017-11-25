package kstn.game.logic.state;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state_event.TransitToLoginState;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicCreatedRoomsState extends LogicGameState {
    private final EventManager eventManager;
    private final ViewManager root;
    private final View backgroundView;

    private final UDPForwarder forwarder;
    private final EventListener roomListener;

    Map<Integer, ExpireProcess> expireProcessMap = new HashMap<>();

    private class ExpireProcess extends Process {
        private int ipAddress;
        private long currentTime;

        public ExpireProcess(int ipAddress) {
            this.ipAddress = ipAddress;
        }

        @Override
        public void onInit() {
            super.onInit();
            currentTime = 0;
        }

        public void reset() {
            currentTime = 0;
        }

        @Override
        public void onUpdate(long deltaMs) {
            currentTime += deltaMs;
            if (currentTime >= 5000)
                succeed();
        }

        @Override
        public void onSuccess() {
            eventManager.trigger(new RemoveCreatedRoomEvent(ipAddress));
            expireProcessMap.remove(new Integer(ipAddress));
        }

        @Override
        public void onFail() {
            onSuccess();
        }

        @Override
        public void onAbort() {
            onSuccess();
        }
    }

    public LogicCreatedRoomsState(EventManager eventManager,
                                  ViewManager root,
                                  View backgroundView,
                                  UDPForwarder forwarder,
                                  final ProcessManager processManager) {
        super(null);
        this.eventManager = eventManager;
        this.root = root;
        this.backgroundView = backgroundView;

        this.forwarder = forwarder;

        roomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SawCreatedRoomEvent event1 = (SawCreatedRoomEvent) event;
                ExpireProcess process = expireProcessMap.get(event1.getIpAddress());
                if (process == null) {
                    process = new ExpireProcess(event1.getIpAddress());
                    expireProcessMap.put(event1.getIpAddress(), process);
                    processManager.attachProcess(process);
                }
                else {
                    process.reset();
                }
            }
        };
    }

    @Override
    public void entry() {
        expireProcessMap.clear();
        root.addView(backgroundView);
        try {
            forwarder.listen();
        } catch (IOException e) {
            eventManager.queue(new TransitToLoginState());
            return;
        }
        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, roomListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.SAW_CREATED_ROOM, roomListener);
        forwarder.shutdown();
        root.removeView(backgroundView);
    }
}
