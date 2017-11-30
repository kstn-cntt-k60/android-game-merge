package kstn.game.logic.state;

import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicRoomCreatorState extends LogicGameState {
    private final ViewManager root;
    private final View backgroundView;
    private final ThisRoom thisRoom;

    public LogicRoomCreatorState(ViewManager root, View backgroundView, ThisRoom thisRoom) {
        super();
        this.root = root;
        this.backgroundView = backgroundView;
        this.thisRoom = thisRoom;
    }

    @Override
    public void entry() {
        thisRoom.entry();
        root.addView(backgroundView);
    }

    @Override
    public void exit() {
        root.removeView(backgroundView);
        thisRoom.exit();
    }
}
