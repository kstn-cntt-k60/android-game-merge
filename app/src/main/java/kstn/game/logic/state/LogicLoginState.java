package kstn.game.logic.state;

import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewManager;

public class LogicLoginState extends LogicGameState {
    private final ImageView backgroundView;
    private final ViewManager root;
    private final ThisPlayer thisPlayer;

    public LogicLoginState(ViewManager root,
                           ImageView backgroundView,
                           ThisPlayer thisPlayer) {
        super(null);
        this.root = root;
        this.thisPlayer = thisPlayer;
        this.backgroundView = backgroundView;
    }

    @Override
    public void entry() {
        thisPlayer.entry();
        root.addView(backgroundView);
    }

    @Override
    public void exit() {
        root.removeView(backgroundView);
        thisPlayer.exit();
    }
}
