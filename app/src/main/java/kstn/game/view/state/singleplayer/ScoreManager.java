package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.BonusEvent;
import kstn.game.logic.playing_event.PlayerStateChangeEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.view.state.ViewStateManager;

public class ScoreManager {
    private ViewStateManager stateManager;
    private SongManager songManager;
    private TextView txtScore;
    private int score;

    private EventListener playerStateListener;
    private EventListener bonusListener;

    public ScoreManager(ViewStateManager stateManager_, final SongManager songManager_) {
        this.stateManager = stateManager_;
        this.songManager = songManager_;
        playerStateListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                PlayerStateChangeEvent event = (PlayerStateChangeEvent)event_;
                int oldScore = score;
                setScore(event.getScore());
                int newScore = score;

                if (newScore > oldScore) {
                    songManager.startTingTing();
                    Toast.makeText(stateManager.activity,
                            "+" + (newScore - oldScore) + " điểm", Toast.LENGTH_SHORT).show();
                }
                else if (newScore < oldScore) {
                    songManager.startFail();
                    Toast.makeText(stateManager.activity,
                            "" + (newScore - oldScore) + " điểm", Toast.LENGTH_SHORT).show();
                }
            }
        };

        bonusListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                BonusEvent event = (BonusEvent)event_;
                songManager.startTingTing();
                increase(event.getBonus());
                Toast.makeText(stateManager.activity,
                        "Bạn đã được thưởng +" + event.getBonus() + " điểm", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void onViewCreated(View view) {
        txtScore = view.findViewById(R.id.txtMoney);
        setScore(0);
    }

    public void increase(int value) {
        assert (value > 0);
        assert (value % 100 == 0);
        score += value;
        txtScore.setText(Integer.toString(score));
    }

    public void setScore(int score) {
        this.score = score;
        txtScore.setText(Integer.toString(score));
    }

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.BONUS, bonusListener);
        stateManager.eventManager.addListener(PlayingEventType.PLAYER_STATE_CHANGE, playerStateListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.PLAYER_STATE_CHANGE, playerStateListener);
        stateManager.eventManager.removeListener(PlayingEventType.BONUS, bonusListener);
    }

}
