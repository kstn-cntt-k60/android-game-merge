package kstn.game.view.thang.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import kstn.game.R;
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.model.QuestionModel;
import kstn.game.logic.playing_event.AnswerEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.ConeResultEvent;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.state.singleplayer.LifeManager;
import kstn.game.view.state.singleplayer.ScoreManager;
import kstn.game.view.state.singleplayer.SongManager;

import static kstn.game.view.state.singleplayer.CharCellManager.dem;

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
    Button btnDoanX, btnHuy;
    TextView txtTraLoi;

    Button btnDoan;
    private Animation scaleAnimation;

    // Listeners
    private EventListener rotateResultListener;
    private EventListener giveAnswerListener;
    private EventListener nextQuestionListener;

    private String question;
    private String answer;

    private boolean coneResultAnimationEnd;
    private boolean requestedGiveAnswer;

    public PlayFragment() {
        rotateResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeResultEvent event = (ConeResultEvent)event_;
                handleRotateResult(event.getResult());
            }
        };

        giveAnswerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                requestedGiveAnswer = true;
                if (coneResultAnimationEnd)
                    handleGiveAnswerEvent();
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

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
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

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.ROTATE_RESULT, rotateResultListener);
        stateManager.eventManager.addListener(PlayingEventType.GIVE_ANSWER, giveAnswerListener);
        stateManager.eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        stateManager.eventManager.removeListener(PlayingEventType.ROTATE_RESULT, rotateResultListener);
        stateManager.eventManager.removeListener(PlayingEventType.GIVE_ANSWER, giveAnswerListener);
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
        txtLevel = (TextView) view.findViewById(R.id.txtLevel);
        txtCauHoi = (TextView) view.findViewById(R.id.txtCauHoi);
        txtNoiDungKim = (TextView) view.findViewById(R.id.txtNoiDungKim);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        guessView = inflater.inflate(R.layout.giaodien_alert_doan, null);
        giveAnswerView = inflater.inflate(R.layout.giaodien_alert_letter, null);

        btnHuy = (Button) guessView.findViewById(R.id.btnHuy);
        btnDoanX = (Button) guessView.findViewById(R.id.btnDoanX);
        txtTraLoi = (TextView) guessView.findViewById(R.id.txtTraLoi);

        keyboardManager.onViewGuessKeyboard(guessView, txtTraLoi, height);
        keyboardManager.onViewGiveAnswer(giveAnswerView, height);

        btnDoan = (Button) view.findViewById(R.id.btnDoan);
        btnDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardManager.showDialogGuess();
                txtTraLoi.setText("");

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        keyboardManager.hideDialogGuess();
                    }
                });

                btnDoanX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (txtTraLoi.getText().toString().equals(answer)) {
                            songManager.startTingTing();
                        } else {
                            songManager.startFail();
                        }
                    }
                });
            }
        });

        scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
        keyboardManager.hideDialogGuess();
    }

    public void handleNextQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
        keyboardManager.resetGiveAnswerKeyboard();
        keyboardManager.hideDialogGuess();
        dem = 0;
        txtCauHoi.setText(question);
    }

    private void handleRotateResult(int result) {
        final String text = ConeResult.getString(result);
        TextView txtDiem = (TextView) giveAnswerView.findViewById(R.id.txtDiem);
        txtDiem.setText(text);
        coneResultAnimationEnd = false;
        requestedGiveAnswer = false;

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtNoiDungKim.setText(text);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (requestedGiveAnswer)
                    handleGiveAnswerEvent();
                coneResultAnimationEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        txtNoiDungKim.startAnimation(scaleAnimation);
    }

    private void handleGiveAnswerEvent() {
        keyboardManager.showDialogGiveAnswer();
        for(int i = 0; i < keyboardManager.getGiveAnswer().size(); i++){
            final Button button = keyboardManager.getGiveAnswer().get(i);
            if (keyboardManager.isActive(i)) {
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleGiveAnswerButton(button, finalI);
                    }
                });
            }else {
                button.setBackgroundColor(Color.GRAY);
                button.setClickable(false);
            }
        };
    }

    private void handleGiveAnswerButton(final Button button, final int index) {
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.setBackgroundColor(Color.YELLOW);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setBackgroundColor(Color.GRAY);
                keyboardManager.deactivate(index);
                keyboardManager.hideDialogGuess();;

                char character = button.getText().toString().charAt(0);
                keyboardManager.hideDialogGiveAnswer();

                stateManager.eventManager.queue(new AnswerEvent(character));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button.startAnimation(scaleAnimation);
    }
}



