package kstn.game.logic.state;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;

/**
 * Created by qi on 13/11/2017.
 */

public class LogicSinglePlayerState extends LogicGameState {
    private int score;
    private int life;
    private List<String> coneCells;
    private String question, answer;
    private EventListener coneStopListener;
    private Cone cone;

    public LogicSinglePlayerState(LogicStateManager stateManager) {
        super(stateManager);
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

        coneStopListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeStopEventData event = (ConeStopEventData) event_;
                coneStopAt(event.getResult());
            }
        };
        cone = new Cone(stateManager.processManager, stateManager.assetManager,
                        stateManager.eventManager,stateManager.timeManager,stateManager.root);

    }

    private void coneStopAt(int index) {
    }

    @Override
    public void entry() {
        Log.i("LogicSingle", "State");
        score = 0;
        life = 3;
        cone.entry();
        stateManager.eventManager.addListener(ConeEventType.STOP, coneStopListener);

    }

    @Override
    public void exit() {
        stateManager.eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        cone.exit();
    }
}
