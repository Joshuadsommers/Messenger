package objects;

import Server.Room;
import enums.ServerRequest;
import objects.Message;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Josh on 8/15/2016.
 */
public class MessageToServer {

    ServerRequest requestType;
    Room room;


    User sender;
    User recipient;

    Message message;

    Timestamp time;

    String roomTitle;
    int roomNumber;

    /**
     *
     * This Constructor is used for @Enum ServerRequest type Private Message.
     * This is called when a @Class User wishes to Private Message one Individual in chat.
     * This request will be forwarded to the Server, and if approved will assign a Unique Room ID and place both Users in a seperate Private Chat Room.
     *
     * @param sender
     *        The @Class Sender of the Request
     * @param recipient
     *        The Recipient that the Server will Delegate the request to.
     *
     */

    public MessageToServer(User sender, User recipient){
        this.requestType = ServerRequest.PRIVATE_MESSAGE;
        this.sender = sender;
        this.recipient = recipient;

    }

    public MessageToServer(User sender, User recipient, Room room){
        this.requestType = ServerRequest.ROOM_INVITE;
        this.sender = sender;
        this.recipient = recipient;
    }

    public MessageToServer(User sender, String roomTitle){
        this.requestType = ServerRequest.ROOM_REQUEST;
        this.sender = sender;
        this.roomTitle = roomTitle;
    }

    public MessageToServer(User sender, String roomTitle, int roomNumber){
        this.requestType = ServerRequest.CUSTOM_ROOM_REQUEST;
        this.sender = sender;
        this.roomTitle = roomTitle;
        this.roomNumber = roomNumber;
    }



    public Message getMessage() {
        return message;
    }

    public Timestamp getTime() {
        return time;
    }

    public MessageToServer(String r, String f, Message m) {
      //  this.room = r;
       // this.from = f;
        this.message = m;

        this.time = new Timestamp(new Date().getTime());
    }


}
