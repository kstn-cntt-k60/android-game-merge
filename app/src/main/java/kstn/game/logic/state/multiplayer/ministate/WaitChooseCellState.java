package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.state.multiplayer.CellManager;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;

public class WaitChooseCellState extends State {
    private final EventManager eventManager;
    private final CellManager cellManager;
    private final MultiPlayerManager multiPlayerManager;

    public WaitChooseCellState(EventManager eventManager,
                               CellManager cellManager,
                               MultiPlayerManager multiPlayerManager) {
        this.eventManager = eventManager;
        this.cellManager = cellManager;
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void chooseCell(int cellIndex) {
    }
}
