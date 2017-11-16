package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.view.state.ViewStateManager;

/**
 * Created by tung on 15/11/2017.
 */

public class KeyboardManager {
    private ViewStateManager stateManager;
    private ArrayList<Button> guessKeyboard;
    private ArrayList<Button> answerKeyboard;
    private DialogManager dialogGuess;
    private DialogManager dialogGiveAnswer;
    private boolean[] isActive = new boolean[27];

    private EventListener giveAnswerListener;

    public KeyboardManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;

        giveAnswerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                showDialogGiveAnswer();
            }
        };
    }

    public void entry() {
        stateManager.eventManager.addListener(PlayingEventType.GIVE_ANSWER, giveAnswerListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(PlayingEventType.GIVE_ANSWER, giveAnswerListener);
    }


    public ArrayList<Button> getGiveAnswer() {
        return answerKeyboard;
    }

    public ArrayList<Button> getGuessKeyboard() {
        return guessKeyboard;
    }

    public void onViewGiveAnswer(View view, int height) {
        dialogGiveAnswer = new DialogManager(stateManager.activity, view, height);
        answerKeyboard = new ArrayList<>();

        answerKeyboard.add((Button) view.findViewById(R.id.btnA));
        answerKeyboard.add((Button) view.findViewById(R.id.btnB));
        answerKeyboard.add((Button) view.findViewById(R.id.btnC));
        answerKeyboard.add((Button) view.findViewById(R.id.btnD));

        answerKeyboard.add((Button) view.findViewById(R.id.btnE));
        answerKeyboard.add((Button) view.findViewById(R.id.btnF));
        answerKeyboard.add((Button) view.findViewById(R.id.btnG));
        answerKeyboard.add((Button) view.findViewById(R.id.btnH));

        answerKeyboard.add((Button) view.findViewById(R.id.btnI));
        answerKeyboard.add((Button) view.findViewById(R.id.btnJ));
        answerKeyboard.add((Button) view.findViewById(R.id.btnK));
        answerKeyboard.add((Button) view.findViewById(R.id.btnL));

        answerKeyboard.add((Button) view.findViewById(R.id.btnM));
        answerKeyboard.add((Button) view.findViewById(R.id.btnN));
        answerKeyboard.add((Button) view.findViewById(R.id.btnO));
        answerKeyboard.add((Button) view.findViewById(R.id.btnP));

        answerKeyboard.add((Button) view.findViewById(R.id.btnQ));
        answerKeyboard.add((Button) view.findViewById(R.id.btnR));
        answerKeyboard.add((Button) view.findViewById(R.id.btnS));
        answerKeyboard.add((Button) view.findViewById(R.id.btnT));

        answerKeyboard.add((Button) view.findViewById(R.id.btnU));
        answerKeyboard.add((Button) view.findViewById(R.id.btnV));
        answerKeyboard.add((Button) view.findViewById(R.id.btnW));
        answerKeyboard.add((Button) view.findViewById(R.id.btnX));

        answerKeyboard.add((Button) view.findViewById(R.id.btnY));
        answerKeyboard.add((Button) view.findViewById(R.id.btnZ));

        dialogGiveAnswer.getHopthoai().setCancelable(false);
        dialogGiveAnswer.getHopthoai().setCanceledOnTouchOutside(false);

    }

    public void showDialogGiveAnswer(){
        dialogGiveAnswer.getHopthoai().show();
    }

    public void showDialogGuess(){
        dialogGuess.getHopthoai().show();
    }

    public DialogManager getDialogGuess() {
        return dialogGuess;
    }

    public DialogManager getDialogGiveAnswer() {
        return dialogGiveAnswer;
    }

    public void DisActive(int i){
        isActive[i] = false;
    }

    public boolean Active(int i){
        return isActive[i];
   }

    public void onViewGuessKeyboard(View view, final TextView txt, int height) {
        dialogGuess = new DialogManager(stateManager.activity, view, height);
        guessKeyboard = new ArrayList<>();

        guessKeyboard.add((Button) view.findViewById(R.id.btnQ));
        guessKeyboard.add((Button) view.findViewById(R.id.btnW));
        guessKeyboard.add((Button) view.findViewById(R.id.btnE));
        guessKeyboard.add((Button) view.findViewById(R.id.btnR));
        guessKeyboard.add((Button) view.findViewById(R.id.btnT));
        guessKeyboard.add((Button) view.findViewById(R.id.btnY));
        guessKeyboard.add((Button) view.findViewById(R.id.btnU));
        guessKeyboard.add((Button) view.findViewById(R.id.btnI));
        guessKeyboard.add((Button) view.findViewById(R.id.btnO));
        guessKeyboard.add((Button) view.findViewById(R.id.btnP));
        guessKeyboard.add((Button) view.findViewById(R.id.btnA));
        guessKeyboard.add((Button) view.findViewById(R.id.btnS));
        guessKeyboard.add((Button) view.findViewById(R.id.btnD));
        guessKeyboard.add((Button) view.findViewById(R.id.btnF));
        guessKeyboard.add((Button) view.findViewById(R.id.btnG));
        guessKeyboard.add((Button) view.findViewById(R.id.btnH));
        guessKeyboard.add((Button) view.findViewById(R.id.btnJ));
        guessKeyboard.add((Button) view.findViewById(R.id.btnK));
        guessKeyboard.add((Button) view.findViewById(R.id.btnL));
        guessKeyboard.add((Button) view.findViewById(R.id.btnZ));
        guessKeyboard.add((Button) view.findViewById(R.id.btnX));
        guessKeyboard.add((Button) view.findViewById(R.id.btnC));
        guessKeyboard.add((Button) view.findViewById(R.id.btnV));
        guessKeyboard.add((Button) view.findViewById(R.id.btnB));
        guessKeyboard.add((Button) view.findViewById(R.id.btnN));
        guessKeyboard.add((Button) view.findViewById(R.id.btnM));
        guessKeyboard.add((Button) view.findViewById(R.id.btnSpace));
        guessKeyboard.add((Button) view.findViewById(R.id.btnBackSpace));
        for (final Button btn : guessKeyboard) {
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
        dialogGuess.getHopthoai().show();

    }

    public void resetGiveAnswerKeyboard(MainActivity activity) {
        int i=0;
        for (Button btn : answerKeyboard) {
            isActive[i] = true;
            i++;
            btn.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_mini));
        }
    }
}




