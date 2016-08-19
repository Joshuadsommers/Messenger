package enums;

import Client.ConnectionHandler;
import Client.RoomHandler;
import objects.Command;
import objects.MasterClass;

import java.util.ArrayList;

/**
 * Created by Adam on 8/18/2016.
 */
public final class CommandHandler {

    private static final String CLEAR_SCREEN = "cls";

    private static ArrayList<String> commands = loadCommands();

    private static ArrayList<String> loadCommands(){
        commands = new ArrayList<>();
        commands.add("cls");
        return commands;
    }

    public static void receiveCommand(Command command){

        System.out.println(command.getCommand());
        String commandValue = command.getCommand();
        int key = command.getKey();
        switch(commandValue){

            case CLEAR_SCREEN:
                clearScreen(key);
                break;

            default:
                System.out.println("No case statement for command: [" + commandValue + "]");
                break;
        }

    }

    public static void sendCommand(String command){

        CommandType type = getCommandType(command);
        String commandValue = getCommandValue(command);

        if(verifyCommand(commandValue)){
            if(type.equals(CommandType.SERVER_COMMAND)) sendServerCommand(commandValue);
            else sendClientCommand(commandValue);
        }
        else MasterClass.client.addInformationMessage("[ERROR] Invalid Command Entered: [" + command + "]");


    }

    private static boolean verifyCommand(String value){
        return commands.contains(value);
    }

    private static CommandType getCommandType(String command){
        return command.startsWith("--") ? CommandType.SERVER_COMMAND : CommandType.CLIENT_COMMAND;
    }


    private static String getCommandValue(String rawCommand){
        return rawCommand.substring(rawCommand.lastIndexOf("-") + 1);
    }

    private static void sendClientCommand(String command){
        switch(command){
            case CLEAR_SCREEN:
                clearScreen(0);
                break;

            default:
                System.out.println("No case statement for command: [" + command + "]");
                break;
        }
    }


    private static void clearScreen(int key){
        MasterClass.client.clearScreen(key);
    }



    private static void sendServerCommand(String value){
        Command command = new Command(MasterClass.user, value, RoomHandler.getCurrentRoomKey());
        ConnectionHandler.sendCommand(command);
    }


}
