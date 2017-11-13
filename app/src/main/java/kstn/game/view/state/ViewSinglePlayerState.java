package kstn.game.view.state;

/**
 * Created by qi on 13/11/2017.
 */

public class ViewSinglePlayerState extends ViewGameState {

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {

    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {

    }
}
