package kstn.game.view.state.multiplayer;

import kstn.game.logic.state.IEntryExit;

public class WaitRoomProxy implements IEntryExit {
    private final IWaitRoom waitRoom;

    public WaitRoomProxy(IWaitRoom waitRoom) {
        this.waitRoom = waitRoom;
    }

    @Override
    public void entry() {
    }

    @Override
    public void exit() {
    }
}
