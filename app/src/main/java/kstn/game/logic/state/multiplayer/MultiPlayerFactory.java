package kstn.game.logic.state.multiplayer;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.data.ManagerDAO;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.logic.state.multiplayer.ministate.RotatableState;
import kstn.game.logic.state.multiplayer.ministate.RotatingState;
import kstn.game.logic.state.multiplayer.ministate.WaitAnswerState;
import kstn.game.logic.state.multiplayer.ministate.WaitChooseCellState;
import kstn.game.logic.state.multiplayer.ministate.WaitGuessResultState;
import kstn.game.logic.state.multiplayer.ministate.WaitOtherPlayersState;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class MultiPlayerFactory {
    private final LogicStateManager stateManager;
    private final WifiInfo wifiInfo;
    private final NetworkForwarder networkForwarder;
    private final ViewManager root;
    private final View backgroundView;
    private final Cone cone;

    private final ThisRoom thisRoom;
    private final ThisPlayer thisPlayer;

    public MultiPlayerFactory(LogicStateManager stateManager,
                              WifiInfo wifiInfo,
                              NetworkForwarder networkForwarder,
                              ViewManager root,
                              View backgroundView,
                              Cone cone,
                              ThisPlayer thisPlayer,
                              ThisRoom thisRoom
    ) {
        this.stateManager = stateManager;
        this.wifiInfo = wifiInfo;
        this.networkForwarder = networkForwarder;
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
        managerDAO.open();

        QuestionManager questionManager = new QuestionManager(
                stateManager.eventManager,
                managerDAO
        );

        CellManager cellManager = new CellManager(
                stateManager.eventManager
        );

        LevelManager levelManager = new LevelManager(
                stateManager.eventManager,
                questionManager,
                scorePlayerManager
        );

        MultiPlayerManager playerManager = new MultiPlayerManager(
                stateManager.eventManager,
                scorePlayerManager,
                questionManager,
                cellManager,
                levelManager,
                wifiInfo,
                networkForwarder
        );

        // Init All States
        WaitOtherPlayersState waitOtherPlayersState = new WaitOtherPlayersState(
                scorePlayerManager,
                playerManager,
                cone
        );
        RotatableState rotatableState = new RotatableState(
                stateManager.eventManager,
                cellManager, playerManager, cone
        );

        RotatingState rotatingState = new RotatingState(
                stateManager.eventManager,
                scorePlayerManager,
                playerManager
        );

        WaitAnswerState waitAnswerState = new WaitAnswerState(
                stateManager.eventManager,
                cellManager,
                scorePlayerManager,
                playerManager
        );

        WaitGuessResultState waitGuessResultState = new WaitGuessResultState(
                stateManager.eventManager,
                questionManager,
                levelManager,
                scorePlayerManager,
                cellManager,
                playerManager
        );

        WaitChooseCellState waitChooseCellState = new WaitChooseCellState(
                cellManager, playerManager
        );

        // Set up dependencies
        playerManager.setWaitOtherPlayersState(waitOtherPlayersState);
        playerManager.setRotatableState(rotatableState);

        waitOtherPlayersState.setRotatableState(rotatableState);

        rotatableState.setRotatingState(rotatingState);
        rotatableState.setWaitGuessResultState(waitGuessResultState);

        rotatingState.setWaitOtherPlayersState(waitOtherPlayersState);
        rotatingState.setRotatableState(rotatableState);
        rotatingState.setWaitChooseCellState(waitChooseCellState);
        rotatingState.setWaitAnswerState(waitAnswerState);

        waitAnswerState.setWaitOtherPlayersState(waitOtherPlayersState);
        waitAnswerState.setRotatableState(rotatableState);
        waitAnswerState.setRotatingState(rotatingState);

        waitChooseCellState.setRotatableState(rotatableState);
        waitChooseCellState.setWaitGuessResultState(waitGuessResultState);

        waitGuessResultState.setWaitOtherPlayersState(waitOtherPlayersState);
        waitGuessResultState.setRotatableState(rotatableState);

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
