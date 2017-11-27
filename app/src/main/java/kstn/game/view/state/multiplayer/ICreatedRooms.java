package kstn.game.view.state.multiplayer;

public interface ICreatedRooms {
    void addRoom(int ipAddress, String roomName, int numberOfPlayers);

    void removeRoom(int ipAddress);
}
