package kstn.game.app.root;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

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
import kstn.game.view.asset.AssetManager;
import kstn.game.view.cone.Cone;
import kstn.game.view.cone.Needle;
import kstn.game.view.network.ClientFactory;
import kstn.game.view.network.ServerFactory;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewGroup;

public class Root implements GameViewClient {

    private float start = 0;
    private final String TAG = getClass().getSimpleName();

	private final GameAnimationView gameView;
    private final Context context;
    private final Activity activity;

    private BaseEventManager eventManager;
    private EventManager uiEventManager;

    private BaseProcessManager processManager;
    private LLBaseEventManager llEventManager;
    private AssetManager assetManager;

    private ClientFactory clientFactory;
    private ServerFactory serverFactory;

    private BaseTimeManager timeManager;
    private long previousTimeStamp = 0;

    private Cone gameCone;
    private Needle gameNeedle;

    public EventManager getUiEventManager() {
        return uiEventManager;
    }

    public Root(Activity activity, GameAnimationView gameView) {
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

        Bitmap background = null;
        try {
            background = assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            assert (false);
        }
        final ImageView backgroundView = new ImageView(0, 0, 16.0f / 3 , 4, background);

        viewGroup.addView(backgroundView);

        gameNeedle = new Needle(processManager, assetManager, eventManager, viewGroup);

        gameCone = new Cone(processManager, assetManager, eventManager, timeManager, gameNeedle, viewGroup);
        gameCone.show();
        gameNeedle.show();
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
