package kstn.game.view.thang.fragment;

import android.graphics.Color;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.model.CauHoiModel;
import kstn.game.logic.playing_event.OverCellEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.RotateResultEvent;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.state.singleplayer.LifeManager;
import kstn.game.view.state.singleplayer.ScoreManager;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.data.QuestionManagerDAO;

import static kstn.game.view.state.singleplayer.CharCellManager.dem;

public class PlayFragment extends Fragment {
    private ViewStateManager stateManager;
    private SongManager songManager;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;
    private CharCellManager charCellManager;
    private TextView txtLevel;
    private CauHoiModel cauhoi;
    private TextView txtCauHoi;
    private TextView txtNoiDungKim;
    private  KeyboardManager keyboardManager;

    // Database
    private QuestionManagerDAO questionManagerDAO;
    private ArrayList<CauHoiModel> dataCauHoi;

    Random rd = new Random();
    int height;
    View guessView, giveAnswerView;
    Button btnDoanX, btnHuy;
    TextView txtTraLoi;

    Button btnDoan;

    // Listeners
    private EventListener rotateResultListener;
    private EventListener giveChooseCellListener;

    public PlayFragment() {
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


        // khởi tạo trình quản lí câu hỏi SQL lite
        questionManagerDAO = new QuestionManagerDAO(getActivity());
        questionManagerDAO.open();
        dataCauHoi = questionManagerDAO.getData();
        questionManagerDAO.close();

        keyboardManager = new KeyboardManager();
        keyboardManager.onViewGuessKeyboard((MainActivity) getActivity(),guessView,txtTraLoi,height);
        keyboardManager.onViewGiveAnswer((MainActivity) getActivity(),giveAnswerView,height);
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
                        if (txtTraLoi.getText().toString().equals(cauhoi.getCauTraLoi())) {
                            songManager.startTingTing();
                            // stateManager.eventManager.queue(new NextQuestionEvent(cauhoi.getId()));
                            // handleNextQuestion(dataCauHoi.get(rd.nextInt(dataCauHoi.size())));
                        } else {
                            songManager.startFail();
                            scoreManager.decrease(1000);
                            if (lifeManager.count() > 0) {
                                lifeManager.decrease(1);
                            } else {
                                //imgNon.setEnabled(false);
                                Toast.makeText(getActivity(), "game Over", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        rotateResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                RotateResultEvent event = (RotateResultEvent)event_;
                handleRotateResult(event.getResult());
            }
        };

        giveChooseCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                charCellManager.startLuckyChooseCellListener();
            }
        };
    }



    public void handleNextQuestion(String question, String answer) {
        keyboardManager.resetGiveAnswerKeyboard(stateManager.activity);
        keyboardManager.getDialogGuess().getHopthoai().dismiss();
        dem = 0;
        txtCauHoi.setText(question);
    }

    private void handleRotateResult(final String result) {
        final Animation scale = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtNoiDungKim.setText(result);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (txtNoiDungKim.getText().toString().equals("Chia 2")) {
                    scoreManager.divideByHalf();
                    Toast.makeText(getActivity(),"Bạn bị chia 2 số điểm",Toast.LENGTH_SHORT).show();
                    songManager.startFail();
                }
                else if (txtNoiDungKim.getText().toString().equals("Mất Lượt")) {
                    songManager.startFail();

                    if(lifeManager.count() > 0) {
                        lifeManager.decrease(1);
                        Toast.makeText(getActivity(),"Mất Lượt",Toast.LENGTH_SHORT).show();
                    }else{
                        stateManager.eventManager.queue(new OverCellEvent());
                        Toast.makeText(getActivity(),
                                "Bạn đã hết lượt chơi, bạn chỉ có thể đoán luôn ",
                                Toast.LENGTH_SHORT).show();
                    }

                }
                else if (txtNoiDungKim.getText().toString().equals("Nhân 2") ) {
                    scoreManager.x2();
                    Toast.makeText(getActivity(),"Nhân 2 số điểm",Toast.LENGTH_SHORT).show();
                    songManager.startTingTing();
                }
                else if(txtNoiDungKim.getText().toString().equals("Thưởng")){
                    int k = rd.nextInt(2400)+100;
                    Toast.makeText(getActivity(),"Bạn được cộng thêm "+k+" điểm",Toast.LENGTH_SHORT).show();
                    scoreManager.increase(k);
                    songManager.startTingTing();

                }
                else if(txtNoiDungKim.getText().toString().equals("May Mắn")) {
                    Toast.makeText(getActivity(),"Bạn hãy mở 1 ô bạn thích",Toast.LENGTH_SHORT).show();
                }

                else if (txtNoiDungKim.toString().equals("Mất điểm")) {
                    scoreManager.setScore(0);
                    Toast.makeText(getActivity(),"Mất điểm",Toast.LENGTH_SHORT).show();

                    songManager.startFail();
                } else if (txtNoiDungKim.getText().toString().equals("Thêm Lượt")) {
                    lifeManager.increase(1);
                    Toast.makeText(getActivity(),"Bạn được thêm +1 lượt",Toast.LENGTH_SHORT).show();
                    songManager.startTingTing();
                } else {
                    final TextView txtDiem = (TextView) giveAnswerView.findViewById(R.id.txtDiem);
                    txtDiem.setText(txtNoiDungKim.getText().toString());
                    keyboardManager.showDialogGuess();
                    for(int i = 0;i < keyboardManager.getGiveAnswer().size();i++){
                        final Button button = keyboardManager.getGiveAnswer().get(i);
                        if (keyboardManager.Active(i)) {
                            final int finalI = i;
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    handleGiveAnswerButton(button, scale, txtDiem, finalI);
                                }
                            });
                        }else {
                            button.setBackgroundColor(Color.GRAY);
                            button.setClickable(false);
                        }
                    };
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        txtNoiDungKim.startAnimation(scale);
    }

    private void handleGiveAnswerButton(final Button button, Animation scale,
                                        final TextView txtDiem, final int index) {
        scale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.setBackgroundColor(Color.YELLOW);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setBackgroundColor(Color.GRAY);
                keyboardManager.DisActive(index);
                keyboardManager.getDialogGuess().getHopthoai().dismiss();
                int KT= 0;
                Character []Answer = charCellManager.getData_copy();
                for(int k=0;k<Answer.length;k++){
                    if(Answer[k]==button.getText().toString().charAt(0)&&charCellManager.IsOpen(k)==false){
                        KT +=1;
                        scoreManager.increase(Integer.parseInt(txtDiem.getText().toString()));
                        charCellManager.openCellAbsoluteIndex(k);
                        charCellManager.setIsOpen(k);

                        if(charCellManager.isOverCell()){
                            stateManager.eventManager.queue(new OverCellEvent());
                            Toast.makeText(getActivity(),
                                    "Bạn đã hãy Đoán Luôn để đến câu hỏi tiếp theo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                if(KT!=0){
                    songManager.startTingTing();
                    Toast.makeText(getActivity(),"+ "+KT+"x"+txtDiem.getText().toString(),
                            Toast.LENGTH_SHORT).show();
                }
                if(KT==0){
                    songManager.startFail();

                    if (lifeManager.count() > 0) {
                        lifeManager.decrease(1);
                        Toast.makeText(getActivity(),"Mất 1 Lượt chơi",Toast.LENGTH_SHORT).show();
                    }else{
                        stateManager.eventManager.queue(new OverCellEvent());
                        Toast.makeText(getActivity(),
                                "Bạn đã hết lượt chơi, bạn chỉ có thể đoán luôn ",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button.startAnimation(scale);
    }

}



