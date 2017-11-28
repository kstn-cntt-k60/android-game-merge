package kstn.game.view.state;

import kstn.game.view.thang.fragment.WaitRoomFragment;

public class ViewWaitRoomState extends  ViewGameState {
    public ViewWaitRoomState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        WaitRoomFragment fragment = new WaitRoomFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        postEntry();
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {
        preExit();
    }
}
