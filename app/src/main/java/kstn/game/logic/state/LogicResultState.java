package kstn.game.logic.state;

import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicResultState extends LogicGameState {
    private final ViewManager root;
    private final View backgroundView;

    public LogicResultState(ViewManager root, View backgroundView) {
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
