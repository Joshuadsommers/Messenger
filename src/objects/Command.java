package objects;

import Server.ServerMessage;

/**
 * Created by Adam on 8/18/2016.
 */
public class Command extends ServerMessage {

    private String command;
    private int key;

    public Command(User user, String command, int key){
        super(user);
        this.command = command;
        this.key = key;
    }

    public String getCommand() {
        return command;
    }

    public int getKey() {
        return key;
    }

}
