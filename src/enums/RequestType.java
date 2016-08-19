package enums;

import java.io.Serializable;

/**
 * Created by Adam on 8/16/2016.
 */
public enum RequestType implements Serializable {

    PRIVATE_MESSAGE, ROOM_INVITE, ROOM_REQUEST, CUSTOM_ROOM_REQUEST, CLOSE_ROOM, CLEAR_SCREEN, ALERT

}
