package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.MainActivity;
import kstn.game.app.root.BaseTimeManager;
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
    private final LogicGameState playingState = null;
    private final LogicGameState resultState = null;

    // Single SinglePlayerModel
    private final LogicSinglePlayerState singlePlayerState;
    private final LogicSingleResultState singleResultState;

    // Statistics
    private final LogicGameState statState = null;

    // Managers
    private final ViewStateManager viewStateManager;
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
    }

    public LogicStateManager(ViewStateManager viewStateManager,
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
            background = assetManager.getBitmap("bg.jpg");
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

        roomCreatorState = new LogicRoomCreatorState(root, backgroundView);

        waitRoomState = new LogicWaitRoomState(
                this, eventManager, processManager,
                viewStateManager.waitRoomState,
                root, backgroundView,
                thisPlayer, thisRoom,
                udpForwarder, networkForwarder
        );

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
        prevState = currentState;
        nextState = other;
        currentState.exit();
        currentState = other;
        currentState.entry();
    }
}
