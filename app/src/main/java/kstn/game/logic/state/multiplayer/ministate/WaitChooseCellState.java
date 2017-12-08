package kstn.game.logic.state.multiplayer.ministate;

import kstn.game.logic.state.multiplayer.CellManager;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;

public class WaitChooseCellState extends State {
    private final CellManager cellManager;
    private final MultiPlayerManager multiPlayerManager;

    private State rotatableState;

    public void setRotatableState(State state) {
        rotatableState = state;
    }

    public WaitChooseCellState(CellManager cellManager,
                               MultiPlayerManager multiPlayerManager) {
        this.cellManager = cellManager;
        this.multiPlayerManager = multiPlayerManager;
    }

    @Override
    public void chooseCell(int cellIndex) {
        cellManager.openCell(cellIndex);
        multiPlayerManager.makeTransitionTo(rotatableState);
    }
}
