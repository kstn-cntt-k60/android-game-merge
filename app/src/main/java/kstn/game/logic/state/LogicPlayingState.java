package kstn.game.logic.state;

import kstn.game.logic.cone.Cone;

/**
 * Created by thang on 12/1/2017.
 */

public class LogicPlayingState extends LogicGameState {
    private Cone cone;
    @Override
    public void entry() {
        cone.entry();
    }

    @Override
    public void exit() {
        cone.exit();
    }
}
