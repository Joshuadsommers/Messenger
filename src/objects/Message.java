package objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
<<<<<<< HEAD
 * Created by Josh on 8/15/2016.
=======
 * Created by Adam on 8/14/2016.
>>>>>>> origin/master
 */
public class Message {



    private DateFormat dateFormat = new SimpleDateFormat("[MM/dd/YYYY HH:mm:ss] ");
    private Calendar cal = Calendar.getInstance();

    private User user;

    private int key;

    private boolean timestamp;

    private String rawText;
    private String timeStamp;
    private String message;

    public Message(int key, User user, String rawText, boolean timestamp){
        this.key = key;
        this.user = user;
        this.rawText = rawText;
        this.timestamp = timestamp;

        this.timeStamp = timeStamp();

        format();

    }

    public String getRawText(){
        return this.rawText;
    }

    public User getUser(){
        return this.user;
    }

    private void format(){
        this.message = timestamp ? timeStamp() + "[" + user.getAlias() + "] - " + rawText : "[" + user.getAlias() + "] - " + rawText;
    }

    private String timeStamp(){
        return dateFormat.format(cal.getTime());
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public int getKey() {
        return key;
    }

    public String toString(){
        return "<" + key + ">" + getMessage();
    }
}
