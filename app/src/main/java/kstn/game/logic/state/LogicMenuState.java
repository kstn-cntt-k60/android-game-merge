package kstn.game.logic.state;

import kstn.game.view.screen.ImageView;

/**
 * Created by qi on 13/11/2017.
 */

public class LogicMenuState extends LogicGameState {
    ImageView backgroundView;

    public LogicMenuState(LogicStateManager manager) {
        super(manager);
        // backgroundView = new ImageView(0, 0, 1, 1.6, bitmap);
    }

    @Override
    public void entry() {
        stateManager.root.addView(backgroundView);

    }

    @Override
    public void exit() {
        stateManager.root.removeView(backgroundView);
    }
}
