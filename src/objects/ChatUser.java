package objects;

import Server.Room;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Adam on 8/16/2016.
 */

/**
 *
 *
 * 
 *
 */
public class ChatUser {

    public boolean visible = true;
    private ObjectOutputStream output;
    private User user;
    private ArrayList<Room> activeRooms = new ArrayList<>();

    public ChatUser(User user, ObjectOutputStream output){
        this.user = user;
        this.output = output;
    }

    public void addRoom(Room room){
        activeRooms.add(room);
    }

    public void removeRoom(Room room){
        activeRooms.remove(room);
    }

    public void setVisible(boolean value){
        this.visible = value;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
