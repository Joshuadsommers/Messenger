package enums;

import Client.ConnectionHandler;
import objects.Command;
import objects.MasterClass;

/**
 * Created by Adam on 8/18/2016.
 */
public class CommandHandler {

    public static final String CLEARSCREEN = "-cls";
    public static final String REMOTE_CLEAR_SCREEN = "--cls";



    public static void handleCommand(String command){


        for(CommandType commandType : CommandType.values()){
            System.out.println(CommandType.valueOf(commandType.toString()));
        }

        switch(command){
            case CLEARSCREEN:
                clearScreen();
                break;

            default:
                System.out.println("No case statement for command: [" + command + "]");
                break;
        }
    }

    private static void clearScreen(){
        MasterClass.client.clearScreen();
    }

    private static void sendRemoteCommand(String value){
      //  Command command = new Command(value);
        //ConnectionHandler.sendCommand(command);
    }


}
