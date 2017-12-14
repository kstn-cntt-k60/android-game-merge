package kstn.game.logic.state;

import kstn.game.logic.network.NetworkForwarder;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

public class LogicResultState extends LogicGameState {
    private final ViewManager root;
    private final View backgroundView;
    private final NetworkForwarder networkForwarder;

    public LogicResultState(ViewManager root, View backgroundView,
                            NetworkForwarder networkForwarder) {
        this.root = root;
        this.backgroundView = backgroundView;
        this.networkForwarder = networkForwarder;
    }
    @Override
    public void entry() {
        root.addView(backgroundView);
    }

    @Override
    public void exit() {
        networkForwarder.shutdown();
        root.removeView(backgroundView);
    }
}
