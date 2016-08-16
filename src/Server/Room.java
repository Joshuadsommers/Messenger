package Server;

import enums.ServerMessage;
import objects.ChatUser;
import objects.Message;
import objects.User;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;<<<<<<< HEAD
import objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
=======

>>>>>>> origin/master
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Josh on 8/15/2016.
 */
public class Room  {

    // Room is created from server with all InetAddresses as param
    // When users are added to a room after room is created they will use addUser() and we need to append to addresses

    private int key;

    private String password;
    private boolean passwordProtected = false;

    private HashMap<Integer, ArrayList<ObjectOutputStream>> activeRooms = new HashMap<>(); // Key is Room ID Number, Value is the Array of ObjectOutputStreams going to clients that are connected to that room.

    private HashMap<User, ObjectOutputStream> userStreams = new HashMap<>();

    public static ArrayList<User> activeUsers = new ArrayList<>(); // Global Array of all Active Users
    //public static ArrayList<User> visibleUsers = new ArrayList<>(); // Users that are visible as online.

    private HashSet<ObjectOutputStream> outputStreams = new HashSet<>();

    private String title;

    public Room(int key, String title){
        this.key = key;
        this.title = title;
    }

    public Room(int key, String title, boolean passwordProtected, String password){

        this.key = key;
        this.title = title;
        this.passwordProtected = passwordProtected;
        this.password = password;

    }

    public void sendMessage(Message message){
        outputStreams.forEach(i -> {
            try {
                i.writeObject(message);
                i.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }


    public void addUser(ChatUser user, ObjectOutputStream output){
        userStreams.put(user, output);
    }

    public void removeUser(ChatUser user){
        userStreams.remove(user);
        activeUsers.remove(user);
       // if(user.visible) visibleUsers.remove(user);
    }


    public void addUser(InetAddress a) {
        // Add user to addresses
    }

    public void removeUser(InetAddress a) {
        // Remove user from addresses
    }


    public int getKey() {
        return key;
    }
}
