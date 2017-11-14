package kstn.game.app.root;

import android.content.Context;

import java.io.IOException;

import kstn.game.MainActivity;
import kstn.game.app.asset.AndroidAssetManager;
import kstn.game.app.event.BaseEventManager;
import kstn.game.app.event.LLBaseEventManager;
import kstn.game.app.event.UIEventManager;
import kstn.game.app.network.BaseClientFactory;
import kstn.game.app.network.BaseServerFactory;
import kstn.game.app.process.BaseProcessManager;
import kstn.game.app.screen.GameAnimationView;
import kstn.game.app.screen.GameViewClient;
import kstn.game.app.screen.ShaderProgram;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.view.asset.AssetManager;
import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.Needle;
import kstn.game.view.network.ClientFactory;
import kstn.game.view.network.ServerFactory;
import kstn.game.view.screen.ViewGroup;
import kstn.game.view.state.ViewStateManager;

public class Root implements GameViewClient {

    private float start = 0;

	private final GameAnimationView gameView;
    private final Context context;
    private final MainActivity activity;

    private BaseEventManager eventManager;
    private EventManager uiEventManager;

    private BaseProcessManager processManager;
    private LLBaseEventManager llEventManager;
    private AssetManager assetManager;

    private ClientFactory clientFactory;
    private ServerFactory serverFactory;

    private BaseTimeManager timeManager;
    private long previousTimeStamp = 0;

    /*private Cone gameCone;
    private Needle gameNeedle;*/

    private ViewStateManager viewStateManager = null;
    private LogicStateManager logicStateManager = null;

    public EventManager getUiEventManager() {
        return uiEventManager;
    }

    public Root(MainActivity activity, GameAnimationView gameView) {
        this.activity = activity;
        this.context = activity;
        this.gameView = gameView;
        gameView.setViewClient(this);
    }
	
	public void init() {
        assetManager = new AndroidAssetManager(context);
        try {
            gameView.setProgram(new ShaderProgram(
                    assetManager.getInputStream("shader/UI.vs"),
                    assetManager.getInputStream("shader/UI.fs")
            ));
        } catch (IOException e) {
            e.printStackTrace();
            assert (false);
        }

        llEventManager = new LLBaseEventManager();
        eventManager = new BaseEventManager();
        processManager = new BaseProcessManager();
        clientFactory = new BaseClientFactory(llEventManager);
        serverFactory = new BaseServerFactory(llEventManager);
        timeManager = new BaseTimeManager(llEventManager);
        uiEventManager = new UIEventManager(activity, llEventManager, eventManager);

        final ViewGroup viewGroup = gameView.getRootViewGroup();
        gameView.setLLEventManager(llEventManager);

        viewStateManager = new ViewStateManager(this.activity, uiEventManager);
        logicStateManager = new LogicStateManager(viewGroup, processManager, timeManager,
                eventManager, assetManager);
    }

    @Override
	public void onDrawFrame() {
        llEventManager.update();
        eventManager.update();

        if (previousTimeStamp == 0) {
            previousTimeStamp = timeManager.getCurrentMillis();
            processManager.updateProcesses(0);
        }
        else {
            long currentTimeStamp = timeManager.getCurrentMillis();
            long timeDifferent = currentTimeStamp - previousTimeStamp;
            previousTimeStamp = currentTimeStamp;
            processManager.updateProcesses(timeDifferent);
        }
    }

    public boolean onBack() {
        return viewStateManager.onBack();
    }
	
	public void shutdown() {
        processManager.abortAllProcesses(true);
        eventManager.abortAllEvents();
	}

	@Override
    public void onResume() {
        long timeStamp = System.currentTimeMillis();
        llEventManager.queue(new RootResumeEvent(timeStamp));
    }

    @Override
	public void onPause() {
        long timeStamp = System.currentTimeMillis();
        llEventManager.queue(new RootPauseEvent(timeStamp));
    }

}
