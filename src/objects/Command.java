package objects;

import Server.ServerMessage;

/**
 * Created by Adam on 8/18/2016.
 */
public class Command extends ServerMessage {

    private String value;
    private int key;

    public Command(User user, String value, int key){
        super(user);
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

}
