package kstn.game.view.state;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

import kstn.game.R;
import kstn.game.view.state.singleplayer.DialogManager;
import kstn.game.view.thang.fragment.MenuFragment;

public class ViewMenuState extends ViewGameState {
    private MediaPlayer song;
    private boolean songEnded = false;
    private MenuFragment fragment;

    public ViewMenuState(ViewStateManager manager) {
        super(manager);
        song = MediaPlayer.create(stateManager.activity, R.raw.nhac_hieu);
    }

    @Override
    public void entry() {
        fragment = new MenuFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        if (!songEnded) {
            song.start();
        }
    }

    @Override
    public boolean onBack() {
        View viewExit = fragment.getView().inflate(stateManager.activity,R.layout.alert_exit,null);
        final DialogManager dialogManager = new DialogManager(stateManager.activity,viewExit);
        Button btnNo = viewExit.findViewById(R.id.btnNo);
        Button btnYes = viewExit.findViewById(R.id.btnYes);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogManager.getHopthoai().dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                System.exit(0);
            }
        });
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
