package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.player.SinglePlayerStateChangeEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.view.state.ViewStateManager;

public class LifeManager {
    private ViewStateManager stateManager;
    private SongManager songManager;
    private int lifeCount;
    private TextView txtLife;

    private EventListener playerStateListener;

    public LifeManager(ViewStateManager stateManager_, final SongManager songManager_) {
        this.stateManager = stateManager_;
        this.songManager = songManager_;

        playerStateListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                SinglePlayerStateChangeEvent event = (SinglePlayerStateChangeEvent)event_;
                int oldLife = lifeCount;
                setLife(event.getLife());
                int newLife = lifeCount;

                if (newLife == 0 && oldLife != 0) {
                    Toast.makeText(stateManager.activity,
                            "Bạn đã hết lượt chơi, bạn chỉ có thể đoán luôn ",
                            Toast.LENGTH_SHORT).show();
                    songManager.startFail();
                    return;
                }

                if (newLife > oldLife) {
                    Toast.makeText(stateManager.activity,
                            "Bạn được thêm +1 lượt",Toast.LENGTH_SHORT).show();
                }

                if(newLife < oldLife) {
                    songManager.startFail();
                    Toast.makeText(stateManager.activity, "Bạn bị mất Lượt",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
    }

    public void onViewCreated(View view) {
        txtLife = view.findViewById(R.id.txtMang);
        lifeCount = 4;
        txtLife.setText("" + lifeCount);
    }

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.PLAYER_STATE_CHANGE, playerStateListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.PLAYER_STATE_CHANGE, playerStateListener);
    }

    public void setLife(int value) {
        assert (value > 0);
        lifeCount = value;
        txtLife.setText("" + lifeCount);
    }
}
