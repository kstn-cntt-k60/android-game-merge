package kstn.game.view.state;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state.IEntryExit;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.fragment.MultiPlayFragment;

public class ViewPlayingState extends ViewGameState {
    private final EventManager eventManager;
    private List<IEntryExit> entryExitList = new ArrayList<>();
    private CharCellManager charCellManager;
    private KeyboardManager keyboardManager;
    private SongManager songManager;
    private MultiPlayFragment fragment;

    public ViewPlayingState(EventManager eventManager) {
        super(null);
        this.eventManager = eventManager;
        songManager = new SongManager(stateManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);
    }

    public void addEntryExit(IEntryExit entryExit) {
        entryExitList.add(entryExit);
    }

    @Override
    public void entry() {
        for (IEntryExit e: entryExitList)
            e.entry();
         fragment = new MultiPlayFragment();
        fragment.setStateManager(stateManager);
        fragment.setCharCellManager(charCellManager);
        fragment.setKeyboardManager(keyboardManager);
        fragment.setSongManager(songManager);
        fragment.entry();
        keyboardManager.entry();
        songManager.entry();
        charCellManager.entry();
        stateManager.activity.addFragment(fragment);
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
        fragment.exit();
        keyboardManager.exit();
        songManager.exit();
        charCellManager.exit();
    }
}
