package kstn.game.view.state.multiplayer;

public interface IPlayerManager {
    void setNumberPlayer(int num);

    void setAvatar(int playerIndex, int avatarId);

    void setName(int playerIndex, String name);

    void nextPlayer(int playerIndex);

    void setScore(int value);

    void activatePlayer(int playerIndex);

    void deactivatePlayer(int playerIndex);
}
