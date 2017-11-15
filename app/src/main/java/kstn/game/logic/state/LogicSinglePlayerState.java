package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.view.screen.ImageView;

/**
 * Created by qi on 13/11/2017.
 */

public class LogicSinglePlayerState extends LogicGameState {
    private int score;
    private int life;
    private List<String> coneCells;
    private String question, answer;
    private EventListener overCellListener;
    private Cone cone;
    private ImageView backgroundView;

    private void initConeCells() {
        coneCells = new ArrayList<>(20);
        coneCells.add("800");
        coneCells.add("Mất điểm");
        coneCells.add("100");
        coneCells.add("200");
        coneCells.add("Nhân 2");
        coneCells.add("300");
        coneCells.add("400");
        coneCells.add("May Mắn");
        coneCells.add("300");
        coneCells.add("700");
        coneCells.add("Mất Lượt");
        coneCells.add("600");
        coneCells.add("Chia 2");
        coneCells.add("500");
        coneCells.add("100");
        coneCells.add("Thêm Lượt");
        coneCells.add("200");
        coneCells.add("300");
        coneCells.add("Thưởng");
        coneCells.add("900");
    }

    public LogicSinglePlayerState(LogicStateManager stateManager) {
        super(stateManager);

        initConeCells();

        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
        overCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                cone.disable();
            }
        };

        cone = new Cone(stateManager.processManager, stateManager.assetManager,
                        stateManager.eventManager, stateManager.timeManager, stateManager.root);
    }

    @Override
    public void entry() {
        score = 0;
        life = 3;
        Log.i("ve non", "etst");
        stateManager.root.addView(backgroundView);
        cone.entry();
    }

    @Override
    public void exit() {
        cone.exit();
        stateManager.root.removeView(backgroundView);
    }
}
