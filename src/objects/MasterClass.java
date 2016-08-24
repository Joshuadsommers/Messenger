package objects;

import Client.TerminalController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Adam on 8/14/2016.
 */
public final class MasterClass {

    private static DateFormat dateFormat = new SimpleDateFormat("[MM/dd/YYYY HH:mm:ss] ");
    private static Calendar cal = Calendar.getInstance();

    public static User user = new User("Syndicate");
    public static TerminalController client;

    public static String timeStamp(){
        cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

}
