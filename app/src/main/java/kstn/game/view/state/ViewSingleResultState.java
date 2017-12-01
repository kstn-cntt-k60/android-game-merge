package kstn.game.view.state;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.ResultGameOverEvent;
import kstn.game.view.thang.fragment.ResultFragment;

public class ViewSingleResultState extends ViewGameState {
    private int score;
    private ResultFragment fragment;
    EventListener resultListener;
    public ViewSingleResultState(ViewStateManager stateManager) {
        super(stateManager);
        resultListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ResultGameOverEvent event1 = (ResultGameOverEvent)event;
                score = event1.getScore();
                fragment.setScore(score);
            }
        };
    }

    @Override
    public void entry() {
        stateManager.eventManager.addListener(
                PlayingEventType.GAME_OVER, resultListener);
        fragment = new ResultFragment();
        fragment.setStateManager(stateManager);
        stateManager.activity.addFragment(fragment);
        super.postEntry();
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void exit() {
        super.preExit();
        stateManager.eventManager.removeListener(
                PlayingEventType.GAME_OVER, resultListener);
    }
}
