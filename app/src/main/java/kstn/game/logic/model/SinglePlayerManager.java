package kstn.game.logic.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.data.QuestionManagerDAO;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.AnswerEvent;
import kstn.game.logic.playing_event.BonusEvent;
import kstn.game.logic.playing_event.CellChoosenEvent;
import kstn.game.logic.playing_event.GiveAnswerEvent;
import kstn.game.logic.playing_event.GiveChooseCellEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.OpenCellEvent;
import kstn.game.logic.playing_event.OpenMultipleCellEvent;
import kstn.game.logic.playing_event.PlayerStateChangeEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.RotateResultEvent;
import kstn.game.logic.state.LogicStateManager;

public class SinglePlayerManager {
    private LogicStateManager stateManager;

    private String question;
    private String answer;

    private Player player;
    private int state;
    private int playerLife;

    private QuestionManagerDAO questionManager;

    private final static int NON_ROTATABLE = 0;
    private final static int ROTATABLE = 1;
    private final static int ROTATING = 2;
    private final static int END_ROTATION = 3;

    private EventListener viewReadyListener;
    private EventListener coneAccelListener;
    private EventListener coneStopListener;
    private EventListener answerListener;
    private EventListener cellChosenListener;

    private List<String> coneCells;
    private int rotatedScore;

    private Random random = new Random();

    private Cone cone;

    public void initConeCells() {
        coneCells = new ArrayList<>(20);
        coneCells.add("800");
        coneCells.add("900");
        coneCells.add("Thưởng");
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

    public SinglePlayerManager(
            Cone cone_,
            LogicStateManager stateManager_) {
        this.stateManager = stateManager_;
        this.cone = cone_;
        questionManager = new QuestionManagerDAO(stateManager.mainActivity);
        questionManager.open();

        initConeCells();

        viewReadyListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                getNewQuestion();
            }
        };

        coneAccelListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                switch (state) {
                    case ROTATABLE:
                        state = ROTATING;
                        break;
                }
            }
        };

        coneStopListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                switch (state) {
                    case ROTATING:
                        state = END_ROTATION;
                        ConeStopEventData event = (ConeStopEventData)event_;
                        int result = event.getResult();
                        handleResult(result);
                        cone.disable();
                        break;
                }
            }
        };

        answerListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                switch (state) {
                    case END_ROTATION:
                        AnswerEvent event = (AnswerEvent)event_;
                        handleAnswerCharacter(event.getCharacter());
                        break;
                }
            }
        };

        cellChosenListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                switch (state) {
                    case END_ROTATION:
                        CellChoosenEvent event = (CellChoosenEvent) event_;
                        stateManager.eventManager.trigger(new OpenCellEvent(event.getIndex()));
                        cone.enable();
                        state = ROTATABLE;
                        break;
                }
            }
        };
    }

    private int countCharOccurrences(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++)
            if (str.charAt(i) == ch)
                count++;
        return count;
    }

    private void handleAnswerCharacter(char ch) {
        int count = countCharOccurrences(answer, ch);
        if (count > 0) {
            player.increaseScore(count * rotatedScore);
            stateManager.eventManager.trigger(new OpenMultipleCellEvent(ch));
        }
        else {
            if (playerLife > 0) {
                playerLife--;
            }
            else {
                stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
                state = NON_ROTATABLE;
                return;
            }
        }
        stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
        state = ROTATABLE;
        cone.enable();
    }

    private void getNewQuestion() {
        QuestionModel questionModel = questionManager.getRandomQuestion();
        stateManager.eventManager.trigger(new NextQuestionEvent(
                questionModel.getQuestion(), questionModel.getAnswer()));
        this.question = questionModel.getQuestion();
        this.answer = questionModel.getAnswer();
    }

    private void handleResult(int result) {
        String str = coneCells.get(result);
        stateManager.eventManager.trigger(new RotateResultEvent(str));
        if (str.equals("100")) {
            rotatedScore = 100;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("200")) {
            rotatedScore = 200;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("300")) {
            rotatedScore = 300;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("400")) {
            rotatedScore = 400;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("500")) {
            rotatedScore = 500;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("600")) {
            rotatedScore = 600;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("700")) {
            rotatedScore = 700;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("800")) {
            rotatedScore = 800;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("900")) {
            rotatedScore = 900;
            stateManager.eventManager.trigger(new GiveAnswerEvent());
        }
        else if (str.equals("Mất điểm")) {
            player.lostScore();
            stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
            cone.enable();
            state = ROTATABLE;
        }
        else if (str.equals("Thưởng")) {
            int value = random.nextInt(9);
            int score = 100 + value * 100;
            player.increaseScore(100 + value * 100);
            stateManager.eventManager.trigger(new BonusEvent(score));
            cone.enable();
            state = ROTATABLE;
        }
        else if (str.equals("Mất Lượt")) {
            if (playerLife > 0) {
                playerLife--;
                stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
                state = ROTATABLE;
            }
            else {
                cone.enable();
                state = NON_ROTATABLE;
            }
        }
        else if (str.equals("Nhân 2")) {
            player.x2Score();
            stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
            cone.enable();
            state = ROTATABLE;
        }
        else if (str.equals("Chia 2")) {
            player.divideScoreByHalf();
            stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
            cone.enable();
            state = ROTATABLE;
        }
        else if (str.equals("May Mắn")) {
            stateManager.eventManager.trigger(new GiveChooseCellEvent());
        }
        else if (str.equals("Thêm Lượt")) {
            playerLife++;
            stateManager.eventManager.trigger(new PlayerStateChangeEvent(player.getScore(), playerLife));
            cone.enable();
            state = ROTATABLE;
        }
    }

    public void entry() {
        cone.enable();
        state = ROTATABLE;
        playerLife = 4;
        player = new Player();

        stateManager.eventManager.addListener(PlayingEventType.VIEW_SINGLE_PLAYER_READY, viewReadyListener);
        stateManager.eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.addListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.addListener(PlayingEventType.ANSWER, answerListener);
        stateManager.eventManager.addListener(PlayingEventType.CELL_CHOSEN, cellChosenListener);
    }

    public void exit() {
        stateManager.eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.removeListener(PlayingEventType.ANSWER, answerListener);
        stateManager.eventManager.removeListener(PlayingEventType.CELL_CHOSEN, cellChosenListener);
        stateManager.eventManager.removeListener(PlayingEventType.VIEW_SINGLE_PLAYER_READY, viewReadyListener);
    }
}
