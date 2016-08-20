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

    public static void writeToRoom(Object object, int key){

            writeObject(object, key);

    }

    private static void writeObject(Object object, int key){

        try{
            activeRooms.get(key).writeObject(object); // Calls the "Send Message" method in that room directly.
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void addUser(ChatUser user, int key){
        getRoom(key).addUser(user);
    }

    public static void removeUser(ChatUser user, int key){
        getRoom(key).removeUser(user);
    }

    public static Room getRoom(int key){
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

    public static HashMap getRooms() {
        return activeRooms;
    }


}
