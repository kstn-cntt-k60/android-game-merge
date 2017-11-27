package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.thang.fragment.CreatedRoomFragment;

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
        CreatedRoomFragment fragment = new CreatedRoomFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        fragment.addRoom(500,"Thang",1);
        fragment.addRoom(501,"QUY",2);
        fragment.addRoom(502,"Tung",2);
        fragment.addRoom(502,"Tung",2);
        fragment.addRoom(502,"Tung",2);

    }
    @Override
    public void exit() {

    }
}
