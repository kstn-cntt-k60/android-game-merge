package kstn.game.logic.state;

import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicRoomCreatorState extends LogicGameState {
    private final ViewManager root;
    private final View backgroundView;

    public LogicRoomCreatorState(ViewManager root, View backgroundView) {
        super();
        this.root = root;
        this.backgroundView = backgroundView;
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
    }

    @Override
    public void exit() {
        root.removeView(backgroundView);
    }
}
