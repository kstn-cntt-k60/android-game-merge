package kstn.game.logic.state.multiplayer;

public interface IThisPlayer {
    String getName();
    int getAvatarId();

    void setIsHost(boolean value);

    boolean isHost();
}
