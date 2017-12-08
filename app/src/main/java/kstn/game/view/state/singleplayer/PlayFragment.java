package kstn.game.view.state.singleplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import kstn.game.R;
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.ConeResultEvent;
import kstn.game.view.state.ViewStateManager;

public class PlayFragment extends Fragment {
    private ViewStateManager stateManager;
    private SongManager songManager;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;
    private CharCellManager charCellManager;
    private TextView txtLevel;
    private TextView txtCauHoi;
    private TextView txtNoiDungKim;
    private  KeyboardManager keyboardManager;

     int height;
    View guessView, giveAnswerView;

    Button btnDoan;
    private Animation scaleAnimation;

    // Listeners
    private EventListener rotateResultListener;
    private EventListener nextQuestionListener;

    public PlayFragment() {
        rotateResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeResultEvent event = (ConeResultEvent)event_;
                handleRotateResult(event.getResult());
            }
        };

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                NextQuestionEvent event = (NextQuestionEvent)event_;
                handleNextQuestion(event.getQuestion(), event.getAnswer());
            }
        };
    }

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.CONE_RESULT, rotateResultListener);
        stateManager.eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        stateManager.eventManager.removeListener(PlayingEventType.CONE_RESULT, rotateResultListener);
    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;

        songManager = new SongManager(stateManager);
        scoreManager = new ScoreManager(stateManager, songManager);
        lifeManager = new LifeManager(stateManager, songManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);
    }

    public void setSongManager(SongManager songManager) {
        this.songManager = songManager;
    }

    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    public void setLifeManager(LifeManager lifeManager) {
        this.lifeManager = lifeManager;
    }

    public void setCharCellManager(CharCellManager charCellManager) {
        this.charCellManager = charCellManager;
    }

    public void setKeyboardManager(KeyboardManager keyboardManager) {
        this.keyboardManager = keyboardManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_play, container, false);
        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songManager.onViewCreated(view);
        scoreManager.onViewCreated(view);
        lifeManager.onViewCreated(view);
        charCellManager.onViewCreated(view);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // ánh xạ các biến
        txtLevel = view.findViewById(R.id.txtLevel);
        txtCauHoi = view.findViewById(R.id.txtCauHoi);
        txtNoiDungKim = view.findViewById(R.id.txtNoiDungKim);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        guessView = inflater.inflate(R.layout.giaodien_alert_doan, null);
        giveAnswerView = inflater.inflate(R.layout.giaodien_alert_letter, null);

        keyboardManager.onViewGuessKeyboard(guessView, height);
        keyboardManager.onViewGiveAnswer(giveAnswerView, height);

        btnDoan = view.findViewById(R.id.btnDoan);
        btnDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardManager.requestGuess();
            }
        });

        scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
    }

    public void handleNextQuestion(String question, String answer) {
        txtCauHoi.setText(question);
    }

    private void handleRotateResult(int result) {
        final String text = ConeResult.getString(result);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtNoiDungKim.setText(text);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        txtNoiDungKim.startAnimation(scaleAnimation);
    }
}
