package Server;

import enums.InformationType;
import objects.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

/**
 * Created by Josh on 8/15/2016.
 */

// Room is created from server with all InetAddresses as param
// When users are added to a room after room is created they will use addUser() and we need to append to addresses

/** ------------Author: Adam Goins-------------------------------------------------
 *
 * The Function of this class is to hold a HashSet of active @Class ChatUsers and send messages localized to only members in the room.
 * The Room Class holds a Hashset of @Class ChatUsers that reflects all participating members in the chatroom.
 * Messages are Filtered through the @Class ConnectionManager > @FinalClass RoomHandler > @FinalClass RoomHandler and relayed appropriately using the @Method sendMessage method.
 *
 */

public class Room  {

    private int key; // The room's unique ID
    private String title; // Title of the Room.

    private String password; // The password for a password protected room.
    private boolean passwordProtected = false;


    public static HashSet<ChatUser> activeUsers = new HashSet<>(); // Global Array of all Active Users


    /**
     *
     * @param key
     *       Sets the Unique Room Identifier (int) to whatever key was generated by the @FinalClass RoomHandlerHandler.KeyGenerator() Method.
     *
     * @param title
     *        Title of the Room to be displayed wherever the Developer desires on the User Interface
     *
     * NOTE: This Constructor is used for rooms that do not require password protection
     */
    public Room(int key, String title){
        this.key = key;
        this.title = title;
    }

    /**
     *
     * @param key
     *        Sets the Unique Room Identifier (int) to whatever key was generated by the @FinalClass RoomHandlerHandler.KeyGenerator() Method.
     *
     * @param title
     *        Title of the Room to be displayed wherever the Developer desires on the User Interface
     *
     * @param passwordProtected
     *        Boolean used to reflect that this instance of @FinalClass RoomHandler is Password Protected.
     *
     * @param password
     *        The @String Password required to access this instance of @FinalClass RoomHandler.
     *
     *
     * NOTE: This Constructor is used for the purpose of a Password Protected instance of a @FinalClass RoomHandler.
     */

    public Room(int key, String title, boolean passwordProtected, String password){

        this.key = key;
        this.title = title;
        this.passwordProtected = passwordProtected;
        this.password = password;

    }

    /**
     * @Method sendMessage receives a @Class Message parameter and writes it to every user in the @FinalClass RoomHandler.
     * @param message The @Class Message that was filtered through the @FinalClass ConnectionManager to reach this @FinalClass RoomHandler instance.
     *
     */
    public void sendMessage(Message message){


        activeUsers.forEach(i -> { // For each user in this room
            if(!(message.getUser().equals(i.getUser()))) { // if user is not the one who sent message
                try {
                    i.getOutput().writeObject(message); // Write to the Client's OutputStream the message received from the @FinalClass ConnectionManager
                    i.getOutput().flush(); // Flushes the stream to ensure all bytes were sent across.
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void writeObject(Object object){

        if(object.getClass().equals(Message.class)){
            Message message = (Message) object;
            sendMessage(message);
        }

        else if(object.getClass().equals(Command.class)){
            Command command = (Command) object;
            sendMessage(command);
        }

        else if(object.getClass().equals(InformationMessage.class)){
            sendToAll(object);
        }

        else if(object.getClass().equals(HashSet.class)) {
            sendToAll(object);
        }


    }



    public void sendToAll(Object object){
        activeUsers.forEach(i -> { // For each user in this room
            try {
                i.getOutput().writeObject(object); // Write to the Client's OutputStream the message received from the @FinalClass ConnectionManager
                i.getOutput().flush();             // Flushes the stream to ensure all bytes were sent across.
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
    /**
     * @Method sendMessage receives a @Class Message parameter and writes it to every user in the @FinalClass RoomHandler.
     * @param command The @Class Command that was filtered through the @FinalClass ConnectionManager to reach this @FinalClass RoomHandler instance.
     *
     */
    public void sendMessage(Command command){


        activeUsers.forEach(i -> { // For each user in this room
            if(!(command.getUser().equals(i.getUser()))) { // if user is not the one who sent message
                try {
                    i.getOutput().writeObject(command); // Write to the Client's OutputStream the message received from the @FinalClass ConnectionManager
                    i.getOutput().flush(); // Flushes the stream to ensure all bytes were sent across.
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    /**
     * @Method addUser is used to add a new instance of @Class ChatUser to the @HashSet activeUsers, joining them to this instance of @FinalClass RoomHandler
     *
     * @param user
     *        Instance of @Class ChatUser that has joined this instance of @FinalClass RoomHandler
     */

    public void addUser(ChatUser user){
        InformationMessage newUserMessage = new InformationMessage("User: [" + user.getUser().getAlias() + "] has Connected.", user.getUser(), key, InformationType.USER_JOINED);
        writeObject(newUserMessage);
        activeUsers.add(user); // Adds new user to room
        updateLists();

    }

    /**
     * @Method removeUser removes a user from the room.
     * Note: If there are no more users left in the room, the room closes itself and the instance destroyed.
     *
     * @param user
     *        The @Class ChatUser that will be removed from the @FinalClass RoomHandler
     */

    public void removeUser(ChatUser user){

        activeUsers.remove(user);



        if(activeUsers.size() == 0 && getKey() != 0){
            closeRoom(); // Closes this instance of Room
        }

        else {
            InformationMessage newUserMessage = new InformationMessage("User: [" + user.getUser().getAlias() + "] has Disconnected.", user.getUser(), key, InformationType.USER_LEFT);
            writeObject(newUserMessage);
            updateLists();
        }
    }

    public void closeRoom(){
        RoomHandler.removeRoom(this); // Removes this Instance from the @FinalClass RoomHandler
    }

    public int getKey() {
        return key;
    }

    private synchronized void updateLists() {
        HashSet<User> u = new HashSet<>();
        activeUsers.forEach(value -> {
            u.add(value.getUser());
            System.out.println(value.getUser().getAlias());
        });
        writeObject(u);
    }
}
