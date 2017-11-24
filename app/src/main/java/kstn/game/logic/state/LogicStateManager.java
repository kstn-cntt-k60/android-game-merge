package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.MainActivity;
import kstn.game.app.root.BaseTimeManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.ClientFactory;
import kstn.game.logic.network.IUDPForwarder;
import kstn.game.logic.network.ServerFactory;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.IThisPlayer;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.singleplayer.LogicSinglePlayerState;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewGroup;

public class LogicStateManager {
    private LogicGameState prevState;
    private LogicGameState currentState;

    public final LogicMenuState menuState;

    // Multiplayer

    public final LogicLoginState loginState;
    public final LogicGameState createdRoomsState = null;
    public final LogicGameState roomCreatorState = null;
    public final LogicGameState waitRoomState = null;
    public final LogicGameState playingState = null;
    public final LogicGameState resultState = null;

    // Single SinglePlayerModel
    public final LogicSinglePlayerState singlePlayerState;
    public final LogicSingleResultState singleResultState;

    // Statistics
    public final LogicGameState statState = null;

    // Managers
    public final ViewGroup root;
    public final ProcessManager processManager;
    public final BaseTimeManager timeManager;
    public final EventManager eventManager;
    public final AssetManager assetManager;
    public final WifiInfo wifiInfo;
    public final MainActivity mainActivity;

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

    }

    public LogicStateManager(ViewGroup root,
                             ProcessManager processManager,
                             BaseTimeManager timeManager,
                             EventManager eventManager,
                             AssetManager assetManager,
                             WifiInfo wifiInfo,
                             UDPManagerFactory udpManagerFactory,
                             ServerFactory serverFactory,
                             ClientFactory clientFactory,
                             MainActivity mainActivity) {
        this.root = root;
        this.processManager = processManager;
        this.timeManager = timeManager;
        this.eventManager = eventManager;
        this.assetManager = assetManager;
        this.wifiInfo = wifiInfo;
        this.mainActivity = mainActivity;

        // States
        menuState = new LogicMenuState(this);
        singlePlayerState = new LogicSinglePlayerState(this);
        singleResultState = new LogicSingleResultState(this);

        Bitmap background = null;
        try {
            background = assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView backgroundView =
                new ImageView(0, 0, 2, 1.8f * 2, background);

        IUDPForwarder udpForwarder = new UDPForwarder(
                eventManager, udpManagerFactory, wifiInfo);

        ThisPlayer thisPlayer = new ThisPlayer(eventManager);
        loginState = new LogicLoginState(
                root, backgroundView, thisPlayer,
                eventManager, udpForwarder
        );

        // ----------------------------------
        listenToAllStateEvents();
        currentState = menuState;
        currentState.entry();
    }

    public LogicGameState getPrevState() {
        return prevState;
    }

    public void makeTransitionTo(LogicGameState other) {
        prevState = currentState;
        currentState.exit();
        currentState = other;
        currentState.entry();
    }
}
