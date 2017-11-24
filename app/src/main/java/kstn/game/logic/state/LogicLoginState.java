package kstn.game.logic.state;

import android.util.Log;

import java.io.IOException;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.IUDPForwarder;
import kstn.game.logic.network.UDPManagerFactory;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.view.screen.ImageView;
import kstn.game.view.screen.ViewManager;

public class LogicLoginState extends LogicGameState {
    private final ImageView backgroundView;
    private final ViewManager root;
    private final ThisPlayer thisPlayer;

    private EventManager eventManager;
    private IUDPForwarder forwarder;
    private boolean isListened = false;
    private EventListener listener;

    public LogicLoginState(ViewManager root,
                           ImageView backgroundView,
                           ThisPlayer thisPlayer,
                           EventManager eventManager,
                           IUDPForwarder forwarder) {
        super(null);
        this.root = root;
        this.thisPlayer = thisPlayer;
        this.backgroundView = backgroundView;
        this.eventManager = eventManager;
        this.forwarder = forwarder;

        listener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                SawCreatedRoomEvent event1 = (SawCreatedRoomEvent) event;
                Log.i("Login", "Room: " + event1.getRoomName());
            }
        };
    }

    @Override
    public void entry() {
        thisPlayer.entry();
        root.addView(backgroundView);

        isListened = false;
        try {
            forwarder.listen();
        } catch (IOException e) {
            isListened = true;
            Log.i("Login", "Okay");
        }

        eventManager.addListener(PlayingEventType.SAW_CREATED_ROOM, listener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.SAW_CREATED_ROOM, listener);
        root.removeView(backgroundView);
        thisPlayer.exit();
    }
}
