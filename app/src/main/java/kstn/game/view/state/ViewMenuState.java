package kstn.game.view.state;

import android.media.MediaPlayer;

import kstn.game.R;
import kstn.game.view.thang.fragment.MenuFragment;

public class ViewMenuState extends ViewGameState {
    private MediaPlayer song;
    private boolean songEnded = false;

    public ViewMenuState(ViewStateManager manager) {
        super(manager);
        song = MediaPlayer.create(stateManager.activity, R.raw.nhac_hieu);
    }

    @Override
    public void entry() {
        MenuFragment fragment = new MenuFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        if (!songEnded) {
            song.start();
        }
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {
        if (!songEnded) {
            song.stop();
            songEnded = true;
        }
    }
}
