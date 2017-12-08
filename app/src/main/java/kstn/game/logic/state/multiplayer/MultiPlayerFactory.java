package kstn.game.logic.state.multiplayer;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.data.ManagerDAO;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.logic.state.multiplayer.ministate.RotatableState;
import kstn.game.logic.state.multiplayer.ministate.RotatingState;
import kstn.game.logic.state.multiplayer.ministate.WaitOtherPlayersState;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class MultiPlayerFactory {
    private final LogicStateManager stateManager;
    private final WifiInfo wifiInfo;
    private final ViewManager root;
    private final View backgroundView;
    private final Cone cone;

    private final ThisRoom thisRoom;
    private final ThisPlayer thisPlayer;

    public MultiPlayerFactory(LogicStateManager stateManager,
                              WifiInfo wifiInfo,
                              ViewManager root,
                              View backgroundView,
                              Cone cone,
                              ThisPlayer thisPlayer,
                              ThisRoom thisRoom
    ) {
        this.stateManager = stateManager;
        this.wifiInfo = wifiInfo;
        this.root = root;
        this.backgroundView = backgroundView;
        this.cone = cone;

        this.thisRoom = thisRoom;
        this.thisPlayer = thisPlayer;
    }

    public LogicPlayingState create() {
        ScorePlayerManager scorePlayerManager = new ScorePlayerManager(
                stateManager.eventManager,
                wifiInfo,
                thisRoom
        );

        ManagerDAO managerDAO = new ManagerDAO(stateManager.mainActivity);

        QuestionManager questionManager = new QuestionManager(
                stateManager.eventManager,
                managerDAO
        );

        CellManager cellManager = new CellManager(
                stateManager.eventManager
        );

        LevelManager levelManager = new LevelManager(
                stateManager.eventManager
        );

        MultiPlayerManager playerManager = new MultiPlayerManager(
                stateManager.eventManager,
                scorePlayerManager,
                questionManager,
                cellManager,
                levelManager,
                wifiInfo
        );

        // Init All States
        WaitOtherPlayersState waitOtherPlayersState = new WaitOtherPlayersState(
                scorePlayerManager,
                playerManager
        );
        RotatableState rotatableState = new RotatableState(playerManager);
        RotatingState rotatingState = new RotatingState(
                stateManager.eventManager,
                scorePlayerManager,
                playerManager
        );

        // Set up dependencies
        playerManager.setWaitOtherPlayersState(waitOtherPlayersState);
        playerManager.setRotatableState(rotatableState);

        rotatableState.setRotatingState(rotatingState);

        rotatingState.setWaitOtherPlayersState(waitOtherPlayersState);
        rotatingState.setRotatableState(rotatableState);

        return new LogicPlayingState(
                stateManager,
                stateManager.processManager,
                stateManager.viewStateManager.playingState,
                playerManager,
                root,
                backgroundView,
                cone
        );
    }
}
