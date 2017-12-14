package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.MainActivity;
import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.root.BaseTimeManager;
import kstn.game.logic.cone.Cone;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.ServerFactory;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state.multiplayer.LogicPlayingState;
import kstn.game.logic.state.multiplayer.MultiPlayerFactory;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewGroup;
import kstn.game.view.state.ViewStateManager;

public class LogicStateManager {
    private LogicGameState prevState;
    private LogicGameState currentState;
    private LogicGameState nextState;

    private final LogicMenuState menuState;

    // Multiplayer
    private final LogicLoginState loginState;
    private final LogicCreatedRoomsState createdRoomsState;
    private final LogicRoomCreatorState roomCreatorState;
    private final LogicWaitRoomState waitRoomState;
    private final LogicPlayingState playingState;
    private final LogicGameState resultState;

    // Single SinglePlayerModel
    private final LogicSinglePlayerState singlePlayerState;
    public final LogicSingleResultState singleResultState;

    // bxh
    private final LogicStatState statState;

    // Managers
    public final LLBaseEventManager llEventManager;
    public final ViewStateManager viewStateManager;
    public final ViewGroup root;
    public final ProcessManager processManager;
    public final BaseTimeManager timeManager;
    public final EventManager eventManager;
    public final AssetManager assetManager;
    private final WifiInfo wifiInfo;
    public final MainActivity mainActivity;

    public LogicGameState getCreatedRoomsState() {
        return createdRoomsState;
    }

    public LogicGameState getRoomCreatorState() {
        return roomCreatorState;
    }

    public LogicGameState getPlayingState() { return playingState; }

    public LogicGameState getResultState() { return resultState; }

    private void listenToAllStateEvents() {
        eventManager.addListener(StateEventType.MENU, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(menuState);
            }
        });

        eventManager.addListener(StateEventType.SINGLE_PLAYER, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(singlePlayerState);
            }
        });

        eventManager.addListener(StateEventType.SINGLE_RESULT, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(singleResultState);
            }
        });

        eventManager.addListener(StateEventType.LOGIN, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(loginState);
            }
        });

        eventManager.addListener(StateEventType.CREATED_ROOMS, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(createdRoomsState);
            }
        });

        eventManager.addListener(StateEventType.ROOM_CREATOR, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(roomCreatorState);
            }
        });

        eventManager.addListener(StateEventType.WAIT_ROOM, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(waitRoomState);
            }
        });

        eventManager.addListener(StateEventType.PLAYING, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(playingState);
            }
        });

        eventManager.addListener(StateEventType.RESULT, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(resultState);
            }
        });

        eventManager.addListener(StateEventType.STAT, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                makeTransitionTo(statState);
            }
        });
    }

    public LogicStateManager(LLBaseEventManager llEventManager,
                             ViewStateManager viewStateManager,
                             ViewGroup root,
                             ProcessManager processManager,
                             BaseTimeManager timeManager,
                             EventManager eventManager,
                             AssetManager assetManager,
                             WifiInfo wifiInfo,
                             UDPManagerFactory udpManagerFactory,
                             ServerFactory serverFactory,
                             ClientFactory clientFactory,
                             MainActivity mainActivity) {
        this.llEventManager = llEventManager;
        this.viewStateManager = viewStateManager;
        this.root = root;
        this.processManager = processManager;
        this.timeManager = timeManager;
        this.eventManager = eventManager;
        this.assetManager = assetManager;
        this.wifiInfo = wifiInfo;
        this.mainActivity = mainActivity;

        // States
        menuState = new LogicMenuState(this);
        singlePlayerState = new LogicSinglePlayerState(
                this, processManager, viewStateManager.singlePlayerState);
        singleResultState = new LogicSingleResultState(this);

        Bitmap background = null;
        try {
            background = assetManager.getBitmap("bg2.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView backgroundView =
                new ImageView(0, 0, 2, 1.8f * 2, background);

        UDPForwarder udpForwarder = new UDPForwarder(
                eventManager, udpManagerFactory, wifiInfo);

        NetworkForwarder networkForwarder = new NetworkForwarder(
                eventManager, serverFactory, clientFactory);

        ThisPlayer thisPlayer = new ThisPlayer(eventManager);
        ThisRoom thisRoom = new ThisRoom(eventManager);

        loginState = new LogicLoginState(
                root, backgroundView, thisPlayer);

        createdRoomsState = new LogicCreatedRoomsState(
                eventManager, root, backgroundView,
                thisPlayer, thisRoom,
                udpForwarder, networkForwarder, processManager);

        roomCreatorState = new LogicRoomCreatorState(root, backgroundView, thisRoom);

        Cone cone = new Cone(
                processManager, assetManager,
                eventManager, timeManager,
                root
        );

        ActiveConnections activeConnections = new ActiveConnections();

        waitRoomState = new LogicWaitRoomState(
                this, eventManager, processManager,
                viewStateManager.waitRoomState,
                root, backgroundView,
                thisPlayer, thisRoom,
                udpForwarder, networkForwarder,
                activeConnections,
                cone
        );
        statState = new LogicStatState(this);


        MultiPlayerFactory multiPlayerFactory = new MultiPlayerFactory(
                this,
                wifiInfo, networkForwarder,
                root, backgroundView, cone,
                thisPlayer, thisRoom
        );
        playingState = multiPlayerFactory.create();

        resultState = new LogicResultState(root, backgroundView, networkForwarder);

        // ----------------------------------
        listenToAllStateEvents();
        currentState = menuState;
        currentState.entry();
    }

    public LogicGameState getPrevState() {
        return prevState;
    }

    public LogicGameState getNextState() {
        return nextState;
    }

    public void makeTransitionTo(LogicGameState other) {
        nextState = other;
        currentState.exit();
        prevState = currentState;
        currentState = other;
        currentState.entry();
    }
}
