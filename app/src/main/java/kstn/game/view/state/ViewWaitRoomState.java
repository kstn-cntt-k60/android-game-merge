package kstn.game.view.state;

import kstn.game.view.state.multiplayer.WaitRoomProxy;
import kstn.game.view.thang.fragment.WaitRoomFragment;

public class ViewWaitRoomState extends  ViewGameState {
    private WaitRoomProxy proxy;

    public ViewWaitRoomState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        WaitRoomFragment fragment = new WaitRoomFragment();
        proxy = new WaitRoomProxy(stateManager.eventManager, fragment);
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        fragment.init();

        proxy.entry();
        super.postEntry();
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {
        super.preExit();
        proxy.exit();
        proxy = null;
    }
}
