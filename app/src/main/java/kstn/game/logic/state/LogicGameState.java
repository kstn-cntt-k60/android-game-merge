package kstn.game.logic.state;

import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.view.state.ViewGameState;

public abstract class LogicGameState {
    protected final LogicStateManager stateManager;
    private final ProcessManager processManager;
    private final ViewGameState viewGameState;
    private WaitViewReadProcess process;

    class WaitViewReadProcess extends Process {
        @Override
        public void onInit() {
            super.onInit();
        }

        @Override
        public void onUpdate(long deltaMs) {
            if (viewGameState.isReady())
                succeed();
        }

        @Override
        public void onSuccess() {
            stateManager.llEventManager.update();
            LogicGameState.this.onViewReady();
        }

        @Override
        public void onFail() {}

        @Override
        public void onAbort() {}
    }

    public LogicGameState() {
        this(null, null, null);
    }

    public LogicGameState(LogicStateManager stateManager) {
        this(stateManager, null, null);
    }

    public LogicGameState(LogicStateManager stateManager,
                          ProcessManager processManager,
                          ViewGameState viewGameState) {
        this.stateManager = stateManager;
        this.processManager = processManager;
        this.viewGameState = viewGameState;
    }

    protected void postEntry() {
        if (processManager != null && viewGameState != null) {
            process = new WaitViewReadProcess();
            processManager.attachProcess(process);
        }
    }

    protected void preExit() {
        if (process != null && process.isAlive()) {
            process.fail();
        }
    }

    public abstract void entry();

    protected void onViewReady() {}

    public abstract void exit();
}
