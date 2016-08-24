package Server;

import enums.RequestType;
import objects.User;

/**
 * Created by Adam on 8/24/2016.
 */
public class ServerRequest extends MessageToServer {

    private RequestType type;
    private Room room;

    public ServerRequest(User user) {
        super(user);
    }

    public ServerRequest(User user, RequestType type, Room room){
        super(user);
        this.type = type;
        this.room = room;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
