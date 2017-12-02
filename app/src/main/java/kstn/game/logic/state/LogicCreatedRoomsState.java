package kstn.game.logic.state;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ClickRoomEvent;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.TransitToLoginState;
import kstn.game.logic.state_event.TransitToWaitRoom;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicCreatedRoomsState extends LogicGameState {
    private final EventManager eventManager;
    private final ViewManager root;
    private final View backgroundView;

    private final ThisPlayer thisPlayer;
    private final ThisRoom thisRoom;

    private final UDPForwarder udpForwarder;
    private final NetworkForwarder networkForwarder;
    private final EventListener sawRoomListener;
    private final EventListener clickRoomListener;
    private final EventListener acceptRoomListener;

    private int roomIpAddress;
    private String roomName;

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
                                  ThisPlayer thisPlayer,
                                  ThisRoom thisRoom,
                                  UDPForwarder udpForwarder,
                                  NetworkForwarder networkForwarder,
                                  final ProcessManager processManager) {
        super(null);
        this.eventManager = eventManager;
        this.root = root;
        this.backgroundView = backgroundView;

        this.thisPlayer = thisPlayer;
        this.thisRoom = thisRoom;
        this.udpForwarder = udpForwarder;
        this.networkForwarder = networkForwarder;

        sawRoomListener = new EventListener() {
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

        clickRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                onClickRoomEvent((ClickRoomEvent) event);
            }
        };

        acceptRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                onAcceptRoomEvent((AcceptJoinRoomEvent) event);
            }
        };
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        expireProcessMap.clear();

        thisPlayer.entry();
        thisRoom.clear();
        thisRoom.entry();
        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, sawRoomListener);
        eventManager.addListener(PlayingEventType.CLICK_ROOM, clickRoomListener);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptRoomListener);

        try {
            udpForwarder.listen();
        } catch (IOException e) {
            eventManager.queue(new TransitToLoginState());
        }
    }

    @Override
    public void exit() {
        udpForwarder.shutdown();

        eventManager.removeListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptRoomListener);
        eventManager.removeListener(PlayingEventType.CLICK_ROOM, clickRoomListener);
        eventManager.removeListener(PlayingEventType.SAW_CREATED_ROOM, sawRoomListener);
        thisRoom.exit();
        thisPlayer.exit();
        root.removeView(backgroundView);
    }

    private void onClickRoomEvent(ClickRoomEvent event) {
        try {
            networkForwarder.connect(event.getIpAddress());
        } catch (IOException e) {
            networkForwarder.shutdown();
            return;
        }
        eventManager.trigger(new RequestJoinRoomEvent(
                udpForwarder.getIpAddress(),
                thisPlayer.getName(), thisPlayer.getAvatarId()
        ));
        roomIpAddress = event.getIpAddress();
        roomName = event.getRoomName();
    }

    private void onAcceptRoomEvent(AcceptJoinRoomEvent event) {
        if (event.getNewPlayer().getIpAddress() == udpForwarder.getIpAddress()) {
            eventManager.trigger(new SetThisRoomEvent(roomName, roomIpAddress));
            eventManager.queue(new TransitToWaitRoom());
        }
    }
}
