package Server;

import enums.RequestType;
import objects.User;

/**
 * Created by Adam on 8/24/2016.
 */
public class ServerRequest extends MessageToServer {

    private RequestType type;
    private Room room;
    private Object object;
    public ServerRequest(User user) {
        super(user);
    }

    public ServerRequest(User user, RequestType type, Room room){
        super(user);
        this.type = type;
        this.room = room;
    }

    public ServerRequest(User user, RequestType type, Room room, Object object){
        super(user);
        this.type = type;
        this.room = room;
        this.object = object;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
