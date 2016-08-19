package enums;

import Client.ConnectionHandler;
import objects.Command;
import objects.MasterClass;

/**
 * Created by Adam on 8/18/2016.
 */
public class CommandHandler {

    public static final String CLEARSCREEN = "-cls";
    public static final String REMOVECLEARSCREEN = "--cls";

    public static void handleCommand(String command){
        switch(command){
            case CLEARSCREEN:
                clearScreen();
                break;

            default:
                System.out.println("No Command for command: [" + command + "]");
                break;
        }
    }

    private static void clearScreen(){
        MasterClass.client.clearScreen();
    }

    private static void sendRemoteCommand(String value){
        Command command = new Command(value);
        ConnectionHandler.sendCommand(command);
    }


}
