package objects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */
public class ChatUser extends User {

    public boolean visible = true;


    public void setVisible(boolean value){
        this.visible = value;
    }

}
