package Server;

import enums.InformationType;
import objects.MasterClass;
import objects.User;

import java.io.Serializable;

/**
 * Created by Adam on 8/18/2016.
 */
public class MessageToServer implements Serializable {

    private String timestamp;
    private int key;
    private User user;
    private InformationType type;

    public MessageToServer(User user){
        this.user = user;
        timestamp = MasterClass.timeStamp();
    }

    public MessageToServer(User user, int key, InformationType type){
        this.user = user;
        this.timestamp = MasterClass.timeStamp();
        this.key = key;
        this.type = type;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public User getUser() {
        return user;
    }
}
