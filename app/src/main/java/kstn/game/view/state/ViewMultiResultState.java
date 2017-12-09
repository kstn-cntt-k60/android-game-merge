package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.view.state.multiplayer.GameResultInfo;
import kstn.game.view.thang.fragment.ResultMultiPlayerFragment;

public class ViewMultiResultState extends ViewGameState {
    private ResultMultiPlayerFragment fragment;
    private GameResultInfo gameResultInfo;

    public ViewMultiResultState(ViewStateManager stateManager, GameResultInfo gameResultInfo) {
        super(stateManager);
        this.gameResultInfo = gameResultInfo;
    }

    @Override
    public void entry() {
        fragment = new ResultMultiPlayerFragment();
        fragment.setGameResultInfo(gameResultInfo);
        stateManager.activity.addFragment(fragment);
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToCreatedRoomsState());
        return false;
    }

    @Override
    public void exit() {

    }
}
