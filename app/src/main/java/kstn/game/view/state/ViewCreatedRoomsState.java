package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.state.multiplayer.CreatedRoomsProxy;
import kstn.game.view.thang.fragment.CreatedRoomFragment;

public class ViewCreatedRoomsState extends ViewGameState {
    private CreatedRoomsProxy createdRoomsProxy = null;

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
        createdRoomsProxy = new CreatedRoomsProxy(stateManager.eventManager, fragment);
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);

        createdRoomsProxy.entry();


        fragment.addRoom(500,"Thang",1);
        fragment.addRoom(501,"QUY",2);
        fragment.addRoom(502,"Tung",2);
        fragment.addRoom(502,"Tung",2);
        fragment.addRoom(502,"Tung",2);

    }
    @Override
    public void exit() {
        createdRoomsProxy.entry();
        createdRoomsProxy = null;
    }
}
