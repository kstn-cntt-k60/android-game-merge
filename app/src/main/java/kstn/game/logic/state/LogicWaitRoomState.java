package kstn.game.logic.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.cone.Cone;
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
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SetRoomPlayerListEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ActiveConnections;
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
    private final ActiveConnections activeConnections;
    boolean isHost = false;
    private BroadcastProcess process = null;

    private final EventListener requestJoinRoomListener;
    private final EventListener startPlayingListener;
    private final EventListener exitRoomListener;

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
                              final ThisRoom thisRoom,
                              final UDPForwarder udpForwarder,
                              NetworkForwarder networkForwarder,
                              final ActiveConnections activeConnections,
                              Cone cone) {
        super(stateManager, processManager, viewWaitRoomState);
        this.eventManager = eventManager;
        this.processManager = processManager;
        this.root = root;
        this.backgroundView = backgroundView;
        this.thisPlayer = thisPlayer;
        this.thisRoom = thisRoom;
        this.udpForwarder = udpForwarder;
        this.networkForwarder = networkForwarder;
        this.activeConnections = activeConnections;

        requestJoinRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                if (thisRoom.getPlayerList().size() >= 3)
                    return;

                RequestJoinRoomEvent event1 = (RequestJoinRoomEvent) event;
                activeConnections.addConnection(event1.getConnection());
                eventManager.queue(new AcceptJoinRoomEvent(
                        event1.getClientPlayer(),
                        thisRoom.getPlayerList()
                ));
            }
        };

        startPlayingListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                eventManager.queue(new TransitToPlayingState());
            }
        };

        exitRoomListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ExitRoomEvent event1 = (ExitRoomEvent) event;
                if (event1.getPlayerIpAddress() == udpForwarder.getIpAddress()) {
                    eventManager.queue(new TransitToCreatedRoomsState());
                }
                else if (isHost) {
                    // Remove safely the connection
                    activeConnections.removeConnection(event.getConnection());
                }
            }
        };
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        activeConnections.clear();

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
        super.postEntry();
    }

    @Override
    public void exit() {
        super.preExit();
        if (isHost) {
            exitWhenIsHost();
        }
        else {
            exitWhenIsClient();
        }
        thisRoom.exit();
        root.removeView(backgroundView);
    }

    @Override
    protected void onViewReady() {
        if (isHost) {
            Player hostPlayer = new Player(
                    udpForwarder.getIpAddress(), thisPlayer.getName(), thisPlayer.getAvatarId());
            List<Player> playerList = new ArrayList<>();
            playerList.add(hostPlayer);
            thisRoom.addPlayer(hostPlayer);
            eventManager.queue(new SetRoomPlayerListEvent(playerList));
        }
        else {
            List<Player> playerList = new ArrayList<>();
            playerList.addAll(thisRoom.getPlayerList());
            eventManager.queue(new SetRoomPlayerListEvent(playerList));
        }
    }

    void entryWhenIsClient() {
        isHost = false;
        networkForwarder.setOnConnectionErrorListener(
                new Endpoint.OnConnectionErrorListener() {
            @Override
            public void onConnectionError(Connection connection) {
                eventManager.queue(new TransitToCreatedRoomsState());
            }
        });

        entryBoth();
    }

    void exitWhenIsClient() {
        exitBoth();
        if (stateManager.getNextState() != stateManager.getPlayingState()) {
            networkForwarder.shutdown();
        }
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
                        if (activeConnections.isActive(connection)) {
                            eventManager.queue(new TransitToCreatedRoomsState());
                        }
                    }
                }
        );

        eventManager.addListener(PlayingEventType.REQUEST_JOIN_ROOM, requestJoinRoomListener);
        entryBoth();
    }

    void exitWhenIsHost() {
        exitBoth();
        eventManager.removeListener(PlayingEventType.REQUEST_JOIN_ROOM, requestJoinRoomListener);

        if (process != null && process.isAlive())
            process.succeed();

        udpForwarder.shutdown();
        if (stateManager.getNextState() != stateManager.getPlayingState()) {
            networkForwarder.shutdown();
        }
    }

    void entryBoth() {
        eventManager.addListener(PlayingEventType.START_PLAYING, startPlayingListener);
        eventManager.addListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
    }

    void exitBoth() {
        eventManager.removeListener(PlayingEventType.EXIT_ROOM, exitRoomListener);
        eventManager.removeListener(PlayingEventType.START_PLAYING, startPlayingListener);
    }
}
