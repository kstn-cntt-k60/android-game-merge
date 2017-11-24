package kstn.game.view.state;

import kstn.game.view.thang.fragment.WaitRoomFragment;

/**
 * Created by thang on 11/24/2017.
 */

public class ViewWaitRoomState extends  ViewGameState {
    public ViewWaitRoomState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        WaitRoomFragment fragment = new WaitRoomFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);

    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {

    }
}
