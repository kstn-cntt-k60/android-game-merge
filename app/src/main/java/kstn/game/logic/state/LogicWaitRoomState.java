package kstn.game.logic.state;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.Server;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.logic.state_event.TransitToPlayingState;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;
import kstn.game.view.state.ViewGameState;

public class LogicWaitRoomState extends LogicGameState {
    private final EventManager eventManager;
    private final ProcessManager processManager;
    private final ViewManager root;
    private final View backgroundView;
    private final ThisPlayer thisPlayer;
    private final ThisRoom thisRoom;
    private final UDPForwarder udpForwarder;
    private final NetworkForwarder networkForwarder;
    private boolean isHost = false;
    private BroadcastProcess process = null;

    private final EventListener startPlayingListener;

    private class BroadcastProcess extends Process {
        private long currentTime = 0;

        @Override
        public void onInit() {
            super.onInit();
            SawCreatedRoomEvent event = new SawCreatedRoomEvent(
                    thisRoom.getIpAddress(), thisRoom.getRoomName(),
                    thisRoom.getPlayerList().size()
            );
            udpForwarder.sendEvent(event);
        }

        @Override
        public void onUpdate(long deltaMs) {
            currentTime += deltaMs;
            if (currentTime >= 1000) {
                currentTime = 0;
                SawCreatedRoomEvent event = new SawCreatedRoomEvent(
                        thisRoom.getIpAddress(), thisRoom.getRoomName(),
                        thisRoom.getPlayerList().size()
                );
                udpForwarder.sendEvent(event);
            }
        }

        @Override
        public void onSuccess() {
        }

        @Override
        public void onFail() {}

        @Override
        public void onAbort() {}
    }

    public LogicWaitRoomState(LogicStateManager stateManager,
                              final EventManager eventManager,
                              ProcessManager processManager,
                              ViewGameState viewWaitRoomState,
                              ViewManager root, View backgroundView,
                              ThisPlayer thisPlayer,
                              ThisRoom thisRoom,
                              UDPForwarder udpForwarder,
                              NetworkForwarder networkForwarder) {
        super(stateManager, processManager, viewWaitRoomState);
        this.eventManager = eventManager;
        this.processManager = processManager;
        this.root = root;
        this.backgroundView = backgroundView;
        this.thisPlayer = thisPlayer;
        this.thisRoom = thisRoom;
        this.udpForwarder = udpForwarder;
        this.networkForwarder = networkForwarder;

        startPlayingListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                eventManager.queue(new TransitToPlayingState());
            }
        };
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        thisRoom.entry();
        if (stateManager.getPrevState() == stateManager.getCreatedRoomsState()) {
            entryWhenIsClient();
        }
        else if (stateManager.getPrevState() == stateManager.getRoomCreatorState()) {
            entryWhenIsHost();
        }
        else {
            throw new RuntimeException("Can't have another state");
        }
        postEntry();
    }

    void entryWhenIsClient() {
        isHost = false;
        eventManager.addListener(PlayingEventType.START_PLAYING, startPlayingListener);
        networkForwarder.setOnConnectionErrorListener(
                new Endpoint.OnConnectionErrorListener() {
            @Override
            public void onConnectionError(Connection connection) {
                eventManager.queue(new TransitToCreatedRoomsState());
            }
        });
    }

    @Override
    protected void onViewReady() {
        // Add host player
        Player hostPlayer = new Player(
                udpForwarder.getIpAddress(), thisPlayer.getName(), thisPlayer.getAvatarId());
        List<Player> playerList = new ArrayList<>();
        AcceptJoinRoomEvent event = new AcceptJoinRoomEvent(hostPlayer, playerList);
        eventManager.trigger(event);
    }

    void entryWhenIsHost() {
        isHost = true;

        try {
            udpForwarder.listen();
        } catch (IOException e) {
            eventManager.queue(new TransitToMenuState());
            return;
        }

        try {
            networkForwarder.listen();
        } catch (IOException e) {
            eventManager.queue(new TransitToCreatedRoomsState());
            return;
        }
        process = new BroadcastProcess();
        processManager.attachProcess(process);

        networkForwarder.setOnAcceptErrorListener(
                new Server.OnAcceptErrorListener() {
                    @Override
                    public void onAcceptError() {
                        eventManager.queue(new TransitToCreatedRoomsState());
                    }
                }
        );

        networkForwarder.setOnConnectionErrorListener(
                new Endpoint.OnConnectionErrorListener() {
                    @Override
                    public void onConnectionError(Connection connection) {
                        eventManager.queue(new TransitToCreatedRoomsState());
                    }
                }
        );
        eventManager.addListener(PlayingEventType.START_PLAYING, startPlayingListener);
    }


    @Override
    public void exit() {
        preExit();
        if (isHost) {
            exitWhenIsHost();
        }
        else {
            exitWhenIsClient();
        }
        thisRoom.exit();
        root.removeView(backgroundView);
    }

    void exitWhenIsClient() {
        eventManager.removeListener(PlayingEventType.START_PLAYING, startPlayingListener);
        if (stateManager.getNextState() == stateManager.getCreatedRoomsState()) {
            networkForwarder.shutdown();
        }
    }

    void exitWhenIsHost() {
        eventManager.removeListener(PlayingEventType.START_PLAYING, startPlayingListener);
        if (process != null && process.isAlive())
            process.succeed();

        udpForwarder.shutdown();
        if (stateManager.getNextState() == stateManager.getCreatedRoomsState()) {
            networkForwarder.shutdown();
        }
    }
}
