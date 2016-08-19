package enums;

/**
 * Created by Adam on 8/19/2016.
 */
public enum CommandType {

    CLEAR_SCREEN ("-cls");

    private String command;

    CommandType(String command){
        this.command = command;
    }

}
