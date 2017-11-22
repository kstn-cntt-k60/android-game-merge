package kstn.game.view.state.singleplayer;

import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.answer.AnswerEvent;
import kstn.game.logic.playing_event.ConeResultEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.guess.CancelGuessEvent;
import kstn.game.logic.playing_event.guess.GuessResultEvent;
import kstn.game.logic.playing_event.guess.RequestGuessEvent;
import kstn.game.view.state.ViewStateManager;

public class KeyboardManager {
    private ViewStateManager stateManager;

    private ArrayList<Button> answerKeyboard = new ArrayList<>(28);
    private DialogManager dialogGiveAnswer;
    private TextView txtConeResult;
    private Animation scaleAnimation;
    private boolean[] isActive = new boolean[27];

    private DialogManager dialogGuess;
    private ArrayList<Button> guessKeyboard = new ArrayList<>(30);
    private TextView txtGuessResult;
    private Button btnGuess;
    private Button btnCancel;

    private EventListener nextQuestionListener;
    private EventListener coneResultListener;
    private EventListener giveAnswerListener;
    private EventListener acceptRequestGuessListener;

    public KeyboardManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                resetGiveAnswerKeyboard();
            }
        };

        coneResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ConeResultEvent event1 = (ConeResultEvent)event;
                txtConeResult.setText(ConeResult.getString(event1.getResult()));
            }
        };

        giveAnswerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                handleGiveAnswerEvent();
            }
        };

        acceptRequestGuessListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                showDialogGuess();
            }
        };
    }

    public void entry() {
        stateManager.eventManager.addListener(
                PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        stateManager.eventManager.addListener(
                PlayingEventType.ROTATE_RESULT, coneResultListener);
        stateManager.eventManager.addListener(
                PlayingEventType.GIVE_ANSWER, giveAnswerListener);
        stateManager.eventManager.addListener(
                PlayingEventType.ACCEPT_REQUEST_GUESS, acceptRequestGuessListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(
                PlayingEventType.ACCEPT_REQUEST_GUESS, acceptRequestGuessListener);
        stateManager.eventManager.removeListener(
                PlayingEventType.GIVE_ANSWER, giveAnswerListener);
        stateManager.eventManager.removeListener(
                PlayingEventType.ROTATE_RESULT, coneResultListener);
        stateManager.eventManager.removeListener(
                PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void requestGuess() {
        stateManager.eventManager.queue(new RequestGuessEvent());
    }

    public void onViewGiveAnswer(View view, int height) {
        dialogGiveAnswer = new DialogManager(stateManager.activity, view, height);
        scaleAnimation = AnimationUtils.loadAnimation(
                stateManager.activity, R.anim.scale);

        answerKeyboard.clear();
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

        txtConeResult = view.findViewById(R.id.txtConeResult);
        txtConeResult.setText("");

        dialogGiveAnswer.getHopthoai().setCancelable(false);
        dialogGiveAnswer.getHopthoai().setCanceledOnTouchOutside(false);
    }

    public void showDialogGiveAnswer(){
        dialogGiveAnswer.getHopthoai().show();
    }

    public void hideDialogGiveAnswer(){
        dialogGiveAnswer.getHopthoai().dismiss();
    }

    private void showDialogGuess(){
        dialogGuess.getHopthoai().show();
    }

    private void hideDialogGuess(){
        dialogGuess.getHopthoai().dismiss();
    }

    public void deactivate(int i){
        isActive[i] = false;
    }

    public boolean isActive(int i){
        return isActive[i];
   }

    public void onViewGuessKeyboard(View view, int height) {
        dialogGuess = new DialogManager(stateManager.activity, view, height);

        guessKeyboard.clear();
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

        txtGuessResult = view.findViewById(R.id.txtGuessResult);
        for (final Button btn : guessKeyboard) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.btnSpace) {
                        txtGuessResult.setText(txtGuessResult.getText().toString() + " ");
                    }
                    else if (view.getId() != R.id.btnBackSpace) {
                        txtGuessResult.setText(txtGuessResult.getText().toString()
                                + btn.getText().toString());
                    }
                    else if (!txtGuessResult.getText().toString().equals("")){
                        txtGuessResult.setText(txtGuessResult.getText()
                                .subSequence(0, txtGuessResult.getText().length() - 1));
                    }
                }
            });
        }

        btnCancel = view.findViewById(R.id.btnCancel);
        btnGuess = view.findViewById(R.id.btnGuess);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = txtGuessResult.getText().toString();
                stateManager.eventManager.queue(new GuessResultEvent(string));
                txtGuessResult.setText("");
                hideDialogGuess();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new CancelGuessEvent());
                txtGuessResult.setText("");
                hideDialogGuess();
            }
        });
    }

    public void resetGiveAnswerKeyboard() {
        int i = 0;
        for (Button btn : answerKeyboard) {
            isActive[i] = true;
            i++;
            btn.setBackgroundDrawable(stateManager.activity.getResources()
                    .getDrawable(R.drawable.button_mini));
        }
    }

    private void handleGiveAnswerEvent() {
        showDialogGiveAnswer();
        for(int i = 0; i < answerKeyboard.size(); i++){
            final Button button = answerKeyboard.get(i);
            if (isActive(i)) {
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
                deactivate(index);
                char character = button.getText().toString().charAt(0);
                hideDialogGiveAnswer();

                stateManager.eventManager.queue(new AnswerEvent(character));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        button.startAnimation(scaleAnimation);
    }

}
