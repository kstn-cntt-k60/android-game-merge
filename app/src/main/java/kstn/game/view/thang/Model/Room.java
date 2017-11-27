package kstn.game.view.thang.Model;

/**
 * Created by thang on 11/27/2017.
 */

public class Room {
    private  int ipAddress;
    private  String roomName;
    private  int numberOfPlayers;

    public Room(int ipAddress, String roomName, int numberOfPlayers) {
        this.ipAddress = ipAddress;
        this.roomName = roomName;
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
