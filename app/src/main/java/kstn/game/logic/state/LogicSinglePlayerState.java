package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.sync.LogicPlayingReadyEvent;
import kstn.game.logic.playing_event.sync.PlayingReadyEvent;
import kstn.game.logic.playing_event.sync.ViewPlayingReadyEvent;
import kstn.game.view.screen.ImageView;

public class LogicSinglePlayerState extends LogicGameState {
    private Cone cone;
    private ImageView backgroundView;
    private SinglePlayerManager playerManager;

    private EventListener viewReadyListener;

    public LogicSinglePlayerState(final LogicStateManager stateManager) {
        super(stateManager);

        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
        cone = new Cone(stateManager.processManager, stateManager.assetManager,
                        stateManager.eventManager, stateManager.timeManager, stateManager.root);

        playerManager = new SinglePlayerManager(cone, stateManager);

        viewReadyListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ViewPlayingReadyEvent view = (ViewPlayingReadyEvent)event;
                if (view.sawLogicReady()) {
                    stateManager.eventManager.trigger(new PlayingReadyEvent());
                }
                else {
                    stateManager.eventManager.trigger(
                            new LogicPlayingReadyEvent(true)
                    );
                }
            }
        };
    }

    @Override
    public void entry() {
        stateManager.root.addView(backgroundView);
        cone.entry();
        playerManager.entry();

        stateManager.eventManager.addListener(
                PlayingEventType.VIEW_SINGLE_PLAYER_READY,
                viewReadyListener);

        stateManager.eventManager.trigger(
                new LogicPlayingReadyEvent(false));
    }

    @Override
    public void exit() {
        stateManager.eventManager.removeListener(
                PlayingEventType.VIEW_SINGLE_PLAYER_READY,
                viewReadyListener);
        playerManager.exit();
        cone.exit();
        stateManager.root.removeView(backgroundView);
    }
}
