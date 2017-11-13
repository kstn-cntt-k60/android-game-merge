package kstn.game.logic.state;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.screen.ViewGroup;

/**
 * Created by qi on 09/11/2017.
 */

public class LogicStateManager {
    private LogicGameState prevState;
    private LogicGameState currentState;

    public final LogicMenuState menuState;

    // Multiplayer
    public final LogicGameState loginState = null;
    public final LogicGameState createdRoomsState = null;
    public final LogicGameState roomCreatorState = null;
    public final LogicGameState waitRoomState = null;
    public final LogicGameState playingState = null;
    public final LogicGameState resultState = null;

    // Single Player
    public final LogicSinglePlayerState singlePlayerState;
    public final LogicSingleResultState singleResultState;

    // Statistics
    public final LogicGameState statState = null;

    // Managers
    public final ViewGroup root;
    public final EventManager eventManager;
    public final AssetManager assetManager;

    //
    private void initAllStates() {

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

    }

    public LogicStateManager(ViewGroup root,
                             EventManager eventManager,
                             AssetManager assetManager) {
        this.root = root;
        this.eventManager = eventManager;
        this.assetManager = assetManager;

        // States
        menuState = new LogicMenuState(this);
        singlePlayerState = new LogicSinglePlayerState(this);
        singleResultState = new LogicSingleResultState(this);

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
