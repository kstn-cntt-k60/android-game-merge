package kstn.game.view.state;

import kstn.game.MainActivity;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.StateEventType;

public class ViewStateManager {
    private ViewGameState prevState = null;
    private ViewGameState currentState;
    public final ViewMenuState menuState;

    // Multiplayer
    public final ViewLoginState loginState;
    public final ViewGameState createdRoomsState = null;
    public final ViewGameState roomCreatorState = null;
    public final ViewGameState waitRoomState = null;
    public final ViewPlayingState playingState = null;
    public final ViewGameState resultState = null;

    // Single Player
    public final ViewSinglePlayerState singlePlayerState;
    public final ViewSingleResultState singleResultState;

    // Statistics
    public final ViewGameState statState = null;

    // Managers
    public final MainActivity activity;
    public final EventManager eventManager;
    

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

    public ViewStateManager(MainActivity activity,
                            EventManager eventManager) {
        this.activity = activity;
        this.eventManager = eventManager;

        // States
        menuState = new ViewMenuState(this);
        singlePlayerState = new ViewSinglePlayerState(this);
        singleResultState = new ViewSingleResultState(this);
        loginState = new ViewLoginState(this);

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
