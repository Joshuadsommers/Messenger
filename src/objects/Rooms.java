package objects;

import Server.Room;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */
public final class Rooms {

    public static HashMap<Integer, Room> activeRooms = new HashMap<>();
    public static HashSet<User> activeUsers = new HashSet<>();

    public static Room createRoom(String title, String password){
        Room room = new Room(assignKey(), title, true, password);
        activeRooms.put(room.getKey(), room);
        return room;
    }

    public static Room createRoom(String title){
        Room room = new Room(assignKey(), title);
        activeRooms.put(room.getKey(), room);
        return room;
    }

    public static void sendMessage(Message message){
        try{
            activeRooms.get(message.getKey()).sendMessage(message);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addUser(User user, ObjectOutputStream outputStream){

    }

    public static int assignKey(){
        int key = getRandom();
        while(activeRooms.containsKey(key)) key = getRandom();
        return key;
    }

    private static int getRandom(){
        return (int) (Math.random() * 50000) + 1;
    }



}
