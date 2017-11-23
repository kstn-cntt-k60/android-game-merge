package kstn.game.logic.state.multiplayer.ministate;

public abstract class State {
    public void entry() {}

    public void exit() {}

    public void coneAccel(float startAngle, float speed) {}

    public void coneStop(int value) {}

    public void chooseCell(int cellIndex) {}

    public void answer(char ch) {}

    public void requestGuess() {}

    public void guessResult(String result) {}

    public void cancelGuess() {}
}
