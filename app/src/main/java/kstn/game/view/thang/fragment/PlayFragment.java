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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.model.QuestionModel;
import kstn.game.logic.playing_event.AnswerEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.RotateResultEvent;
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
    private QuestionModel cauhoi;
    private TextView txtCauHoi;
    private TextView txtNoiDungKim;
    private  KeyboardManager keyboardManager;

    private ArrayList<QuestionModel> dataCauHoi;

    Random rd = new Random();
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
    private String prevResult;

    public PlayFragment() {
        rotateResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                RotateResultEvent event = (RotateResultEvent)event_;
                handleRotateResult(event.getResult());
            }
        };

        giveAnswerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
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
        // result.setBackgroundColor(Color.parseColor("#00000000"));
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
        // anh xa o letter


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
                        keyboardManager.getDialogGuess().getHopthoai().dismiss();
                    }
                });

                // TODO
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
    }


    public void handleNextQuestion(String question, String answer) {
        Log.i("Fragment", "handleNextQuestion");
        this.question = question;
        this.answer = answer;
        keyboardManager.resetGiveAnswerKeyboard(stateManager.activity);
        keyboardManager.getDialogGuess().getHopthoai().dismiss();
        dem = 0;
        txtCauHoi.setText(question);
    }

    private void handleRotateResult(final String result) {
        prevResult = result;
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtNoiDungKim.setText(result);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String content = txtNoiDungKim.getText().toString();
                if (content.equals("Mất điểm")) {
                    Toast.makeText(getActivity(),"Mất điểm",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        txtNoiDungKim.startAnimation(scaleAnimation);
    }

    private void handleGiveAnswerEvent() {
        TextView txtDiem = (TextView) giveAnswerView.findViewById(R.id.txtDiem);
        txtDiem.setText(prevResult);
        keyboardManager.showDialogGiveAnswer();
        for(int i = 0; i < keyboardManager.getGiveAnswer().size(); i++){
            final Button button = keyboardManager.getGiveAnswer().get(i);
            if (keyboardManager.Active(i)) {
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
                keyboardManager.DisActive(index);
                keyboardManager.getDialogGuess().getHopthoai().dismiss();

                char character = button.getText().toString().charAt(0);
                keyboardManager.getDialogGiveAnswer().getHopthoai().dismiss();

                stateManager.eventManager.queue(new AnswerEvent(character));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button.startAnimation(scaleAnimation);
    }
}



