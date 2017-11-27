package kstn.game.logic.state;

import java.io.IOException;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.logic.state_event.TransitToPlayingState;

public class LogicWaitRoomState extends LogicGameState {
    private final EventManager eventManager;
    private final UDPForwarder udpForwarder;
    private final NetworkForwarder networkForwarder;
    private boolean isHost = false;

    private final EventListener startPlayingListener;

    public LogicWaitRoomState(LogicStateManager stateManager,
                              final EventManager eventManager,
                              UDPForwarder udpForwarder,
                              NetworkForwarder networkForwarder) {
        super(stateManager);
        this.eventManager = eventManager;
        this.udpForwarder = udpForwarder;
        this.networkForwarder = networkForwarder;

        startPlayingListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                eventManager.queue(new TransitToPlayingState());
            }
        };
    }

    @Override
    public void entry() {
        if (stateManager.getPrevState() == stateManager.getCreatedRoomsState()) {
            entryWhenIsClient();
        }
        else if (stateManager.getPrevState() == stateManager.getRoomCreatorState()) {
            entryWhenIsHost();
        }
        else {
            throw new RuntimeException("Can't have another state");
        }
    }

    void entryWhenIsClient() {
        isHost = false;
        eventManager.addListener(PlayingEventType.START_PLAYING, startPlayingListener);
        networkForwarder.setOnConnectionErrorListener(
                new Endpoint.OnConnectionErrorListener() {
            @Override
            public void onConnectionError(Connection connection) {
                eventManager.queue(new TransitToCreatedRoomsState());
            }
        });
    }

    void entryWhenIsHost() {
        isHost = true;
        try {
            udpForwarder.listen();
            networkForwarder.listen();
        } catch (IOException e) {
            eventManager.queue(new TransitToMenuState());
            return;
        }
    }


    @Override
    public void exit() {
        if (isHost) {
            exitWhenIsHost();
        }
        else {
            exitWhenIsClient();
        }
    }

    void exitWhenIsClient() {
        eventManager.removeListener(PlayingEventType.START_PLAYING, startPlayingListener);
        if (stateManager.getNextState() == stateManager.getCreatedRoomsState()) {
            networkForwarder.shutdown();
        }
    }

    void exitWhenIsHost() {
        // udpForwarder.shutdown();
    }

}
