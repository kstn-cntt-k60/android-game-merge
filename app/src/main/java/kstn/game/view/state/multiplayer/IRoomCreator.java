package kstn.game.view.state.multiplayer;

public interface IRoomCreator {

    void addPlayer(int ipAddress, String name, int avatarId);

    void remotePlayer(int ipAddress);

    void disableStartButton();
}
