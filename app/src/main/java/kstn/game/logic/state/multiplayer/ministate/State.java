package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.state.IEntryExit;

public abstract class State implements IEntryExit {
    @Override
    public void entry() {}

    @Override
    public void exit() {}

    public void coneAccel(float startAngle, float speed) {}

    public void coneStop(int coneIndex) {}

    public void chooseCell(int cellIndex) {}

    public void answer(char ch) {}

    public void requestGuess() {}

    public void guessResult(String result) {}

    public void cancelGuess() {}

    public void nextPlayer(int playerIndex) {}
}
