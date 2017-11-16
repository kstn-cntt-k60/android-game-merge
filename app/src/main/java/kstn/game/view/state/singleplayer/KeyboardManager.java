package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.MainActivity;
import kstn.game.R;

/**
 * Created by tung on 15/11/2017.
 */

public class KeyboardManager {
    private ArrayList<Button> KeyBoard_GiveAllAnswer;
    private ArrayList<Button> KeyBoard_GuessCharacter;
    private DialogManager dialogGiveAll;
    private DialogManager dialogGuess;
    private boolean[] isActive = new boolean[26];



    public ArrayList<Button> getKeyBoard_GiveAllAnswer() {
        return KeyBoard_GiveAllAnswer;
    }

    public ArrayList<Button> getKeyBoard_GuessCharacter() {
        return KeyBoard_GuessCharacter;
    }

    public void onViewGuessCreated(MainActivity activity, View view, int height) {
        dialogGuess = new DialogManager(activity, view, height);
        KeyBoard_GuessCharacter = new ArrayList<>();
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnA));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnB));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnC));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnD));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnE));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnF));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnG));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnH));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnI));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnJ));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnK));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnL));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnM));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnN));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnO));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnP));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnQ));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnR));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnS));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnT));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnU));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnV));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnW));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnX));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnY));
        KeyBoard_GuessCharacter.add((Button) view.findViewById(R.id.btnZ));
        dialogGuess.getHopthoai().setCancelable(false);
        dialogGuess.getHopthoai().setCanceledOnTouchOutside(false);

    }
    public  void showDialogGuess(){
        dialogGuess.getHopthoai().show();
    }
    public void showDialogGiveAll(){
        dialogGiveAll.getHopthoai().show();
    }
    public DialogManager getDialogGiveAll() {
        return dialogGiveAll;
    }

    public DialogManager getDialogGuess() {
        return dialogGuess;
    }

    public void DisActive(int i){
        isActive[i] = false;
    }

   public boolean Active(int i){
        return isActive[i];
   }

    public void onViewAllAnswerCreated(MainActivity activity, View view, final TextView txt, int height) {
        dialogGiveAll = new DialogManager(activity, view, height);
        KeyBoard_GiveAllAnswer = new ArrayList<>();

        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnQ));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnW));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnE));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnR));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnT));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnY));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnU));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnI));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnO));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnP));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnA));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnS));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnD));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnF));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnG));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnH));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnJ));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnK));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnL));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnZ));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnX));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnC));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnV));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnB));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnN));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnM));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnSpace));
        KeyBoard_GiveAllAnswer.add((Button) view.findViewById(R.id.btnBackSpace));
        for (final Button btn : KeyBoard_GiveAllAnswer) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() != R.id.btnBackSpace) {
                        txt.setText(txt.getText().toString() + btn.getText().toString());
                    } else if (view.getId() == R.id.btnBackSpace) {
                        txt.setText(txt.getText().subSequence(0, txt.getText().length() - 1));
                    }
                }

            });

        }
        dialogGiveAll.getHopthoai().show();

    }

    public void reSetGuessKeyBoard(MainActivity activity) {
        int i=0;
        for (Button btn : KeyBoard_GuessCharacter) {
            isActive[i] = true;
            i++;
            btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_mini));
        }
    }
}




