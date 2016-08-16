package objects;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Josh on 8/15/2016.
 */
public class MessageToServer {
    String from;
    String room;
    String message;

    Timestamp time;

    public String getFrom() {
        return from;
    }

    public String getRoom() {
        return room;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getTime() {
        return time;
    }

    public MessageToServer(String r, String f, String m) {
        this.room = r;
        this.from = f;
        this.message = m;

        this.time = new Timestamp(new Date().getTime());
    }


}
