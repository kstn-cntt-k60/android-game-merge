package kstn.game.logic.state;

import android.util.Log;

/**
 * Created by qi on 14/11/2017.
 */

public class LogicLoginState extends LogicGameState {

    public LogicLoginState(LogicStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        Log.i(this.getClass().getName(), "LogicLoginState");
    }

    @Override
    public void exit() {

    }
}
