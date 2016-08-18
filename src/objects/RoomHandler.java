package objects;

import Server.Room;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */
public final class RoomHandler {

    public static HashMap<Integer, Room> activeRooms = new HashMap<>();

    public static HashSet<ChatUser> activeUsers = new HashSet<>();

    public static Room createRoom(String title, String password){
        Room room = new Room(assignKey(), title, true, password);
        activeRooms.put(room.getKey(), room);
        return room;
    }

    public static void start(){
        Room globalRoom = new Room(0, "Global Chat");
        activeRooms.put(0, globalRoom);
    }

    public static Room createRoom(String title){
        Room room = new Room(assignKey(), title);
        activeRooms.put(room.getKey(), room);
        return room;
    }

    public static void removeRoom(Room room){
        activeRooms.remove(room.getKey());
    }

    public static void sendMessage(Message message){
        try{
            int key = message.getKey();                // Gets which room the message is going to
            activeRooms.get(key).sendMessage(message); // Calls the "Send Message" method in that room directly.
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void addUser(ChatUser user, int key){
        getRoom(key).addUser(user);
    }

    public static void removeUser(ChatUser user){
        activeUsers.remove(user);
    }

    private static Room getRoom(int key){
        return activeRooms.get(key);
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