package Client;

import RoomPane.RoomPaneController;
import Server.Room;
import Server.SerializableRoom;
import Server.ServerRequest;
import enums.RequestType;
import objects.MasterClass;
import objects.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 8/19/2016.
 */
public final class RoomHandler {

    private static int currentRoomKey = 0;
    private static ArrayList<Room> activeRooms = new ArrayList<>();
    public static HashMap<Integer, RoomPaneController> roomTabs = new HashMap<>();



    public static int getCurrentRoomKey(){
        return currentRoomKey;
    }

    public static void inviteGroup(HashSet<User> group, Room room){
        ServerRequest request = new ServerRequest(room.getOwner(), RequestType.GROUP_INVITE, room, group);
        ConnectionHandler.sendMessage(request);
    }

    public static void invitePerson

    public static void requestRoom(Room room){
        ConnectionHandler.sendMessage(new ServerRequest(MasterClass.user, RequestType.ROOM_REQUEST, room));
    }

    public static void addRoom(SerializableRoom room){
        activeRooms.add(new Room(room));
    }

    public  static void addTab(int key, RoomPaneController controller){
        roomTabs.put(key, controller);
    }

    public static RoomPaneController getTab(int key){
        return roomTabs.get(key);
    }

}
