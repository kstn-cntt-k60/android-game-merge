package kstn.game.logic.state;

import android.util.Log;

import java.io.IOException;

import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeMoveEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewManager;

public class LogicLoginState extends LogicGameState {
    private final ImageView backgroundView;
    private final ViewManager root;
    private final ThisPlayer thisPlayer;

    private EventManager eventManager;
    private NetworkForwarder forwarder;
    private boolean isListened = false;
    private EventListener listener;

    public LogicLoginState(ViewManager root,
                           ImageView backgroundView,
                           ThisPlayer thisPlayer,
                           EventManager eventManager,
                           NetworkForwarder forwarder) {
        super(null);
        this.root = root;
        this.thisPlayer = thisPlayer;
        this.backgroundView = backgroundView;
        this.eventManager = eventManager;
        this.forwarder = forwarder;

        listener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ConeMoveEventData eventData = (ConeMoveEventData) event;
                Log.i("Login", "Angle: " + eventData.getAngle());
            }
        };
    }

    @Override
    public void entry() {
        thisPlayer.entry();
        root.addView(backgroundView);
        try {
            forwarder.listen();
        }
        catch (IOException e) {
            isListened = false;
            Log.i("Login", "Not okay");
        }
        eventManager.addListener(ConeEventType.MOVE, listener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(ConeEventType.MOVE, listener);
        forwarder.shutdown();

        root.removeView(backgroundView);
        thisPlayer.exit();
    }
}
