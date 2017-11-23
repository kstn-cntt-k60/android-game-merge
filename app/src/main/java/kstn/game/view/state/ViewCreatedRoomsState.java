package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.thang.fragment.CreatedRomFragment;

/**
 * Created by thang on 11/23/2017.
 */

public class ViewCreatedRoomsState extends ViewGameState {
    public ViewCreatedRoomsState(ViewStateManager stateManager) {
        super(stateManager);
    }
    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return true;
    }
    public void entry() {
        CreatedRomFragment fragment = new CreatedRomFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
//        stateManager.eventManager.addListener(new  {
//        });

    }
    @Override
    public void exit() {

    }
}
