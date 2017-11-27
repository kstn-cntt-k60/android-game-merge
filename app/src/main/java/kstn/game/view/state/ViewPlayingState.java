package kstn.game.view.state;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state.IEntryExit;

public class ViewPlayingState extends ViewGameState {
    private final EventManager eventManager;
    private List<IEntryExit> entryExitList = new ArrayList<>();

    public ViewPlayingState(EventManager eventManager) {
        super(null);
        this.eventManager = eventManager;
    }

    public void addEntryExit(IEntryExit entryExit) {
        entryExitList.add(entryExit);
    }

    @Override
    public void entry() {
        for (IEntryExit e: entryExitList)
            e.entry();
    }

    @Override
    public boolean onBack() {
        eventManager.queue(new TransitToCreatedRoomsState());
        return true;
    }

    @Override
    public void exit() {
        for (IEntryExit e: entryExitList)
            e.exit();
    }
}
