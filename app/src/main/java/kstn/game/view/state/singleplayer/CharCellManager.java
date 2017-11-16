package kstn.game.view.state.singleplayer;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.CellChoosenEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.OpenCellEvent;
import kstn.game.logic.playing_event.OverCellEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.view.state.ViewStateManager;

public class CharCellManager {
    private ViewStateManager stateManager;
    private ArrayList<TextView> charCells = new ArrayList<>();
    private List<String> coneCells = new ArrayList<>();
    private MainActivity activity;
    private boolean isOpen[]=new boolean[27];
    private boolean isActive[]=new boolean[27];
    private  int lenghtAnswer=0;
    private Character data_copy[];
    public  static  int dem;

    private EventListener nextQuestionListener;
    private EventListener openCellListener;


    public CharCellManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
        this.activity = stateManager.activity;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                NextQuestionEvent event = (NextQuestionEvent)event_;
                handleQuestion(event.getQuestion(), event.getAnswer());
            }
        };

        openCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                OpenCellEvent event = (OpenCellEvent)event_;
                openCell(event.getIndex());
            }
        };
    }

    public ArrayList<TextView> getCharCells() {
        return charCells;
    }

    public void onViewCreated(View view) {
        charCells.add((TextView) view.findViewById(R.id.txt0));
        charCells.add((TextView) view.findViewById(R.id.txt1));
        charCells.add((TextView) view.findViewById(R.id.txt2));
        charCells.add((TextView) view.findViewById(R.id.txt3));
        charCells.add((TextView) view.findViewById(R.id.txt4));
        charCells.add((TextView) view.findViewById(R.id.txt5));
        charCells.add((TextView) view.findViewById(R.id.txt6));
        charCells.add((TextView) view.findViewById(R.id.txt7));
        charCells.add((TextView) view.findViewById(R.id.txt8));
        charCells.add((TextView) view.findViewById(R.id.txt9));
        charCells.add((TextView) view.findViewById(R.id.txt10));
        charCells.add((TextView) view.findViewById(R.id.txt11));
        charCells.add((TextView) view.findViewById(R.id.txt12));
        charCells.add((TextView) view.findViewById(R.id.txt13));
        charCells.add((TextView) view.findViewById(R.id.txt14));
        charCells.add((TextView) view.findViewById(R.id.txt15));
        charCells.add((TextView) view.findViewById(R.id.txt16));
        charCells.add((TextView) view.findViewById(R.id.txt17));
        charCells.add((TextView) view.findViewById(R.id.txt18));
        charCells.add((TextView) view.findViewById(R.id.txt19));
        charCells.add((TextView) view.findViewById(R.id.txt20));
        charCells.add((TextView) view.findViewById(R.id.txt21));
        charCells.add((TextView) view.findViewById(R.id.txt22));
        charCells.add((TextView) view.findViewById(R.id.txt23));
        charCells.add((TextView) view.findViewById(R.id.txt24));
        charCells.add((TextView) view.findViewById(R.id.txt25));
        charCells.add((TextView) view.findViewById(R.id.txt26));

        initConeCells();
    }

    private void initConeCells() {
        coneCells.add("800");
        coneCells.add("900");
        coneCells.add("Thêm Lượt");
        coneCells.add("300");
        coneCells.add("200");
        coneCells.add("Thêm Lượt");
        coneCells.add("100");
        coneCells.add("500");
        coneCells.add("Chia 2");
        coneCells.add("600");
        coneCells.add("Mất Lượt");
        coneCells.add("700");
        coneCells.add("300");
        coneCells.add("May Mắn");
        coneCells.add("400");
        coneCells.add("300");
        coneCells.add("Nhân 2");
        coneCells.add("200");
        coneCells.add("100");
        coneCells.add("Mất điểm");
    }

    private int findFirstDataCopyCharIndex() {
        for (int i = 0; i < data_copy.length; i++)
            if (data_copy[i] != null)
                return i;
        return -1;
    }

    private void handleQuestion(String question, String answer) {
        for (int i = 0; i < 27; i++) {
            charCells.get(i).setBackgroundResource(R.drawable.button_mini);
            charCells.get(i).setText("");
            isOpen[i] = false;
        }

        ArrayList<String> c = toArray(answer);
        ArrayList<Integer> tmp = new ArrayList<>();
        lenghtAnswer = 0;
        dem = 0;
        data_copy = new Character[27];
        for (int i = 0; i < c.size(); i++) {
            lenghtAnswer += c.get(i).length();
            for (int j = 0; j < c.get(i).length(); j++) {
                tmp.add(i * 9 + (9 - c.get(i).length()) / 2 + j);
                data_copy[i * 9 + (9 - c.get(i).length()) / 2 + j] = c.get(i).charAt(j);
            }
        }
        for (int i = 0; i < 27; i++) {
            if (!tmp.contains(i)) {
                charCells.get(i).setBackgroundColor(Color.GRAY);
                data_copy[i] = null;
                isActive[i]= false;
            }
            else isActive[i]= true;
        }
    }

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        stateManager.eventManager.addListener(PlayingEventType.OPEN_CELL, openCellListener);
    }


    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.OPEN_CELL, openCellListener);
        stateManager.eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    private ArrayList<String> toArray(String answer) {
        String[] str = answer.split(" ");
        int len = 0;
        for (int i = 0; i < str.length; i++) len += str[i].length();
        ArrayList<String> c = new ArrayList<>();
        int start = 0;
        int size = 0;
        while (size != len) {
            String buff = new String();
            int dem = 0;
            for (int i = start; i < str.length; i++) {
                if (dem + str[i].length() <= 9) {
                    buff += str[i];
                    dem += str[i].length();
                } else {
                    start = i;
                    break;
                }
            }

            if (!buff.isEmpty()) {
                c.add(buff);
                size += buff.length();
            }
        }
        return c;
    }

    public void openCell(int index){
        int i = index + findFirstDataCopyCharIndex();
        String ch = data_copy[i].toString();
        isOpen[i]=true;
        dem++;
        charCells.get(i).setText(ch);
    }

    public void openCellAbsoluteIndex(int index) {
        int i = index;
        String ch = data_copy[i].toString();
        isOpen[i]=true;
        dem++;
        charCells.get(i).setText(ch);
    }

    public void setIsOpen(int i){
        isOpen[i] = true;
    }

    public  boolean IsOpen(int i){
        return isOpen[i];
    }

    public void startLuckyChooseCellListener(){
        for (int i=0;i<charCells.size();i++){
            if(!IsOpen(i) && isActive[i] && dem < lenghtAnswer){
                final int finalI = i;
                charCells.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stateManager.eventManager.queue(new CellChoosenEvent(finalI - findFirstDataCopyCharIndex()));
                        StopListener();
                        if(isOverCell()){
                            stateManager.eventManager.queue(new OverCellEvent());
                            Toast.makeText(activity,"Bạn đã hãy Đoán Luôn để đến câu hỏi tiếp theo",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }
    }

    public Character[] getData_copy() {
        return data_copy;
    }

    public void StopListener(){
        for(TextView txt : charCells ){
            txt.setClickable(false);
        }
    }
    public boolean isOverCell(){
        return dem ==lenghtAnswer;
    }
}
