package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.thang.fragment.RankingFragment;

/**
 * Created by thang on 12/1/2017.
 */

public class ViewStatState extends ViewGameState {
    public ViewStatState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        RankingFragment fragment = new RankingFragment();
        stateManager.activity.addFragment(fragment);
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return false;
    }

    @Override
    public void exit() {

    }
}
