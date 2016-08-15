package objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Adam on 8/14/2016.
 */
public class Message {

    private DateFormat dateFormat = new SimpleDateFormat("[MM/dd/YYYY HH:mm:ss] ");
    private Calendar cal = Calendar.getInstance();

    private User user;

    private boolean timestamp;

    private String rawText;
    private String timeStamp;
    private String message;

    public Message(User user, String rawText, boolean timestamp){
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
}
