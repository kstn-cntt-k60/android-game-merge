package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;

/**
 * Created by thang on 11/23/2017.
 */

public class ViewRoomCreatorState extends ViewGameState {
    public ViewRoomCreatorState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {

    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return true;
    }

    @Override
    public void exit() {

    }
}
