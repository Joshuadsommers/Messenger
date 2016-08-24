package Client;

import RoomPane.RoomPaneController;
import Server.Room;
import Server.SerializableRoom;
import Server.ServerRequest;
import enums.RequestType;
import objects.MasterClass;

import java.util.ArrayList;
import java.util.HashMap;

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

    public static void requestRoom(Room room){
        ConnectionHandler.sendMessage(new ServerRequest(MasterClass.user, RequestType.ROOM_REQUEST, room));
    }

    public static void addRoom(SerializableRoom room){
        activeRooms.add(new Room(room));
        System.out.println("Room added");
        System.out.println(room.getKey());
    }

    public  static void addTab(int key, RoomPaneController controller){
        roomTabs.put(key, controller);
    }

    public static RoomPaneController getTab(int key){
        return roomTabs.get(key);
    }

}
