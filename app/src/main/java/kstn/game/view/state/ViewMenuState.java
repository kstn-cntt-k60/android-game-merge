package kstn.game.view.state;

import android.util.Log;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.view.thang.fragment.MenuFragment;

/**
 * Created by qi on 13/11/2017.
 */

public class ViewMenuState extends ViewGameState {
    public ViewMenuState(ViewStateManager manager) {
        super(manager);
    }

    @Override
    public void entry() {
        MainActivity activity = stateManager.activity;
        MenuFragment fragment = new MenuFragment();
        fragment.setStateManager(stateManager);
        activity.addFragment(R.id.myLayout, fragment);
        Log.i("ViewMenuState", "meNUView");
    }

    @Override
    public boolean onBack() {
        Log.d("ViewMenuState", "onBack");
        return false;
    }

    @Override
    public void exit() {

    }
}
