package Server;

import objects.Message;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Josh on 8/15/2016.
 */
public class MessageToServer {
    String from;
    String room;
    Message message;

    Timestamp time;

    public String getFrom() {
        return from;
    }

    public String getRoom() {
        return room;
    }

    public Message getMessage() {
        return message;
    }

    public Timestamp getTime() {
        return time;
    }

    public MessageToServer(String r, String f, Message m) {
        this.room = r;
        this.from = f;
        this.message = m;

        this.time = new Timestamp(new Date().getTime());
    }


}
