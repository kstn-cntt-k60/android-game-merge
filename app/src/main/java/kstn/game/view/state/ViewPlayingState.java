package kstn.game.view.state;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;

public class ViewPlayingState extends ViewGameState {
    private final EventManager eventManager;

    public ViewPlayingState(EventManager eventManager) {
        super(null);
        this.eventManager = eventManager;
    }

    @Override
    public void entry() {
        super.postEntry();
    }

    @Override
    public boolean onBack() {
        eventManager.queue(new TransitToCreatedRoomsState());
        return true;
    }

    @Override
    public void exit() {
        super.preExit();
    }
}
