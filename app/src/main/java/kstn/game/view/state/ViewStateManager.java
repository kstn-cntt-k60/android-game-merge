package kstn.game.view.state;

import kstn.game.MainActivity;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.state.multiplayer.GameResultInfo;

public class ViewStateManager {
    private ViewGameState prevState = null;
    private ViewGameState currentState;
    public final ViewMenuState menuState;

    // Multiplayer
    public final ViewLoginState loginState;
    public final ViewCreatedRoomsState createdRoomsState ;
    public final ViewRoomCreatorState roomCreatorState;
    public final ViewWaitRoomState waitRoomState;
    public final ViewPlayingState playingState ;
    public final ViewMultiResultState resultState ;

    // Single SinglePlayerModel
    public final ViewSinglePlayerState singlePlayerState;
    public final ViewSingleResultState singleResultState;

    // Statistics
    public final ViewStatState statState;

    // Managers
    public final MainActivity activity;
    public final EventManager eventManager;
    public final WifiInfo wifiInfo;
    

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

    public ViewStateManager(MainActivity activity,
                            EventManager eventManager,
                            WifiInfo wifiInfo) {
        this.activity = activity;
        this.eventManager = eventManager;
        this.wifiInfo = wifiInfo;

        // States
        menuState = new ViewMenuState(this);
        singlePlayerState = new ViewSinglePlayerState(this);
        singleResultState = new ViewSingleResultState(this);

        loginState = new ViewLoginState(this);
        createdRoomsState = new ViewCreatedRoomsState(this);
        roomCreatorState = new ViewRoomCreatorState(this);
        waitRoomState = new ViewWaitRoomState(this);
        GameResultInfo gameResultInfo = new GameResultInfo();
        playingState = new ViewPlayingState(this, gameResultInfo);
        resultState = new ViewMultiResultState(this, gameResultInfo);
        statState = new ViewStatState(this);
        listenToAllStateEvents();
        currentState = menuState;
        currentState.entry();
    }
    
    public ViewGameState getPrevState() {
        return prevState;
    }

    private void makeTransitionTo(ViewGameState other) {
        prevState = currentState;
        currentState.exit();
        currentState = other;
        currentState.entry();
    }

    public boolean onBack() {
        return currentState.onBack();
    }
}
