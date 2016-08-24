package Server;

import objects.MasterClass;
import objects.User;

import java.io.Serializable;

/**
 * Created by Adam on 8/18/2016.
 */
public class MessageToServer implements Serializable {

    private String timestamp;
    private User user;

    public MessageToServer(User user){
        this.user = user;
        timestamp = MasterClass.timeStamp();
    }

    public String getTimestamp(){
        return timestamp;
    }

    public User getUser() {
        return user;
    }
}
