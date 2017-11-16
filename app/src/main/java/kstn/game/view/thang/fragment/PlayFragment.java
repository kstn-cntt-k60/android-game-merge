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

//    // bien cờ
//    private boolean[] flag = new boolean[26];

    // list o bi mat

    // quan li cau hoi, ket noi sqlite
    private QuestionManagerDAO questionManagerDAO;
    private ArrayList<CauHoiModel> dataCauHoi;
    ArrayList<String> data = new ArrayList<>();
    Random rd = new Random();
    int height;
    View view1, view2;
    Button btnDoanX, btnHuy;
    TextView txtTraLoi;

    Button btnDoan;

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
        charCellManager = new CharCellManager(stateManager, (MainActivity) getActivity());
        charCellManager.onViewCreated(view);

//
//        // khoi tao bien cờ
//        for (int i = 0; i < 26; i++) {
//            flag[i] = true;
//        }
        // get chiều dài rộng mà hình
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // ánh xạ các biến
        txtLevel = (TextView) view.findViewById(R.id.txtLevel);
        txtCauHoi = (TextView) view.findViewById(R.id.txtCauHoi);
        txtNoiDungKim = (TextView) view.findViewById(R.id.txtNoiDungKim);

        // khởi tạo arraylist Điểm vòng quay
        data.add("800");
        data.add("900");
        data.add("Thêm Lượt");
        data.add("300");
        data.add("200");
        data.add("Thêm Lượt");
        data.add("100");
        data.add("500");
        data.add("Chia 2");
        data.add("600");
        data.add("Mất Lượt");
        data.add("700");
        data.add("300");
        data.add("May Mắn");
        data.add("400");
        data.add("300");
        data.add("Nhân 2");
        data.add("200");
        data.add("100");
        data.add("Mất điểm");


        // khởi tạo ds các ô bí mật
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        view1 = inflater.inflate(R.layout.giaodien_alert_doan, null);
        view2 = inflater.inflate(R.layout.giaodien_alert_letter, null);
        // anh xa o letter



        btnHuy = (Button) view1.findViewById(R.id.btnHuy);
        btnDoanX = (Button) view1.findViewById(R.id.btnDoanX);
        txtTraLoi = (TextView) view1.findViewById(R.id.txtTraLoi);


        // khởi tạo trình quản lí câu hỏi SQL lite
        questionManagerDAO = new QuestionManagerDAO(getActivity());
        questionManagerDAO.open();
        dataCauHoi = questionManagerDAO.getData();
        questionManagerDAO.close();

        keyboardManager = new KeyboardManager();
        keyboardManager.onViewAllAnswerCreated((MainActivity) getActivity(),view1,txtTraLoi,height);
        keyboardManager.onViewGuessCreated((MainActivity) getActivity(),view2,height);
        this.UpdateContext(dataCauHoi.get(rd.nextInt(dataCauHoi.size())));
        btnDoan = (Button) view.findViewById(R.id.btnDoan);
        btnDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardManager.showDialogGiveAll();
                txtTraLoi.setText("");

                btnHuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        keyboardManager.getDialogGiveAll().getHopthoai().dismiss();
                    }
                });
                btnDoanX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (txtTraLoi.getText().toString().equals(cauhoi.getCauTraLoi())) {
                            songManager.startTingTing();
                           // stateManager.eventManager.queue(new NextQuestionEvent(cauhoi.getId()));
                            UpdateContext(dataCauHoi.get(rd.nextInt(dataCauHoi.size())));
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

        final int[] result = new int[1];
        stateManager.eventManager.addListener(ConeEventType.STOP, new EventListener() {
            @Override
            public void onEvent(EventData event) {
                result[0] = ((ConeStopEventData) event).getResult();
                songManager.endConeRotation();
                final Animation scale = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
                scale.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        txtNoiDungKim.setText(data.get(result[0]));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (txtNoiDungKim.getText().toString().equals("Chia 2")) {
                            scoreManager.divideByHalf();
                            Toast.makeText(getActivity(),"bạn bị chia 2 số điểm",Toast.LENGTH_SHORT).show();
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

                        } else if (txtNoiDungKim.getText().toString().equals("Nhân 2") ) {
                            scoreManager.x2();
                            Toast.makeText(getActivity(),"Nhân 2 số điểm",Toast.LENGTH_SHORT).show();
                            songManager.startTingTing();
                        }
                        else if(txtNoiDungKim.getText().toString().equals("Thưởng")){
                            int k = rd.nextInt(2400)+100;
                            Toast.makeText(getActivity(),"Bạn được cộng thêm "+k+" điểm",Toast.LENGTH_SHORT).show();
                            scoreManager.increase(k);
                            songManager.startTingTing();
                        }  else if(txtNoiDungKim.getText().toString().equals("May Mắn")){
                            Toast.makeText(getActivity(),"Bạn hãy mở 1 ô bạn thích",Toast.LENGTH_SHORT).show();
                            charCellManager.StartListener();
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
                            final TextView txtDiem = (TextView) view2.findViewById(R.id.txtDiem);
                            txtDiem.setText(txtNoiDungKim.getText().toString());
                            keyboardManager.showDialogGuess();
                            for(int i=0;i<keyboardManager.getKeyBoard_GuessCharacter().size();i++){
                                final Button l =keyboardManager.getKeyBoard_GuessCharacter().get(i);
                                if (keyboardManager.Active(i)) {
                                    final int finalI = i;
                                    l.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    scale.setAnimationListener(new Animation.AnimationListener() {
                                                        @Override
                                                        public void onAnimationStart(Animation animation) {
                                                            l.setBackgroundColor(Color.YELLOW);
                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animation animation) {
                                                            l.setBackgroundColor(Color.GRAY);
                                                            keyboardManager.DisActive(finalI);
                                                            keyboardManager.getDialogGuess().getHopthoai().dismiss();
                                                            int KT= 0;
                                                            Character []Answer = charCellManager.getData_copy();
                                                            for(int k=0;k<Answer.length;k++){
                                                                if(Answer[k]==l.getText().toString().charAt(0)&&charCellManager.IsOpen(k)==false){
                                                                    KT +=1;
                                                                    scoreManager.increase(Integer.parseInt(txtDiem.getText().toString()));
                                                                    charCellManager.OpenCells(k,l.getText().toString());
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
                                                    l.startAnimation(scale);
                                                }

                                            });

                                }else {
                                    l.setBackgroundColor(Color.GRAY);
                                    l.setClickable(false);
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
        });

    }



    public void UpdateContext(CauHoiModel cauhoi) {
        keyboardManager.reSetGuessKeyBoard((MainActivity) getActivity());
        keyboardManager.getDialogGiveAll().getHopthoai().dismiss();
        dem = 0;
        txtCauHoi.setText(cauhoi.getCauhoi());
        charCellManager.entry(cauhoi);
    }




}



