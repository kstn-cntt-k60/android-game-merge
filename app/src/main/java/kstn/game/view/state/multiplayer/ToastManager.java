package kstn.game.view.state.multiplayer;

import android.app.Activity;
import android.widget.Toast;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.ShowToastEvent;
import kstn.game.logic.state.IEntryExit;

public class ToastManager implements IEntryExit {
    private final EventManager eventManager;
    private final EventListener showToastListener;

    public ToastManager(EventManager eventManager,
                        final Activity activity) {
        this.eventManager = eventManager;
        showToastListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                ShowToastEvent showToastEvent= (ShowToastEvent) event;
                Toast.makeText(
                        activity, showToastEvent.getText(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.SHOW_TOAST, showToastListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.SHOW_TOAST, showToastListener);
    }
}
