package Server;

import java.net.InetAddress;

/**
 * Created by Josh on 8/15/2016.
 */
public class Room {

    // Room is created from server with all InetAddresses as param
    // When users are added to a room after room is created they will use addUser() and we need to append to addresses
    public Room(InetAddress... addresses){
        for( InetAddress a : addresses) {
            // send message obj to each client in room
        }
    }

    public void addUser(InetAddress a) {
        // Add user to addresses
    }

    public void removeUser(InetAddress a) {
        // Remove user from addresses
    }
}
