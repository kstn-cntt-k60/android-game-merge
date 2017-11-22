package kstn.game.view.state;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.view.state.multiplayer.IEntryExit;
import kstn.game.view.state.multiplayer.IThisPlayer;

public class ViewPlayingState extends ViewGameState {
    private final EventManager eventManager;
    private List<IEntryExit> entryExitList = new ArrayList<>();
    private final IThisPlayer thisPlayer;


    public ViewPlayingState(EventManager eventManager,
                            IThisPlayer thisPlayer) {
        super(null);
        this.eventManager = eventManager;
        this.thisPlayer = thisPlayer;
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
        eventManager.queue(new TransitToCreatedRoomsState(
                    thisPlayer.getName(), thisPlayer.getAvatarId()));
        return true;
    }

    @Override
    public void exit() {
        for (IEntryExit e: entryExitList)
            e.exit();
    }
}
