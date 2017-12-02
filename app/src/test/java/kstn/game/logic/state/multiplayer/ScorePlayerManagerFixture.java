package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;

public class ScorePlayerManagerFixture {
    EventManager eventManager;
    WifiInfo wifiInfo;
    ThisRoom thisRoom;

    ScorePlayerManager build() {
        return new ScorePlayerManager(eventManager, wifiInfo, thisRoom);
    }
}
