package objects;

import Server.InformationMessage;
import Server.Room;
import Server.SerializableRoom;
import enums.InformationType;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */
public final class RoomHandler {

    public static HashMap<Integer, Room> activeRooms = new HashMap<>();

    public static HashSet<ChatUser> activeUsers = new HashSet<>();

    public static void start(){
        Room globalRoom = new Room(0, "Global Chat");
        activeRooms.put(0, globalRoom);
    }

    public static synchronized void createRoom(Room room){

        if(activeRooms.containsKey(room.getKey())){
            room.setKey(assignKey());
        }
        activeRooms.put(room.getKey(), room);
        returnRoom(room);
    }

    private static synchronized void returnRoom(Room room){

        InformationMessage message = new InformationMessage("Room Created: [" + room.getKey() + "] Owner: [" + room.getOwner().getAlias() + "]", room.getOwner(),InformationType.ROOM_CREATED, room.getSerializedVersion());
        activeUsers.forEach(chatUser -> {
            if(chatUser.getUser().getAlias().equals(message.getUser().getAlias())) {
                writeObject(message, chatUser);
                return;
            }
        });
    }

    private static synchronized void writeObject(Object object, ChatUser user){

        try {
            user.getOutput().writeObject(object); // Write to the Client's OutputStream the message received from the @FinalClass ConnectionManager
            user.getOutput().flush();             // Flushes the stream to ensure all bytes were sent across.
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public static synchronized void removeRoom(Room room){
        activeRooms.remove(room.getKey());
    }

    public static synchronized void writeToRoom(Object object, int key){

        try{
            activeRooms.get(key).writeObject(object); // Calls the "Send Message" method in that room directly.
        } catch(Exception e){
            e.printStackTrace();
        }

    }


    public static synchronized void addUser(ChatUser user, int key){
        activeUsers.add(user);
        getRoom(key).addUser(user);
    }

    public static synchronized void removeUser(ChatUser user, int key){
        activeUsers.remove(user);
        getRoom(key).removeUser(user);
    }

    public static synchronized void removeUser(ChatUser user){
        try {
            activeRooms.values().forEach(value -> {
                Room room = (Room) value;
                if (Room.activeUsers.contains(user)) {
                    room.removeUser(user);
                }
            });
        } catch(ConcurrentModificationException e){
            e.printStackTrace();

        }
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
