package Server;

import objects.Message;
import objects.ServerRequest;
import objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */

public class ConnectionManager implements Runnable {

    private HashMap<Integer, ArrayList<ObjectOutputStream>> activeRooms = new HashMap<>(); // Key is Room ID Number, Value is the Array of ObjectOutputStreams going to clients that are connected to that room.

    private HashMap<User, ObjectOutputStream> userStreams = new HashMap<>();

    public static ArrayList<User> activeUsers = new ArrayList<>(); // Global Array of all Active Users
    public static ArrayList<User> visibleUsers = new ArrayList<>(); // Users that are visible as online.


    public static HashSet<ObjectOutputStream> globalOutput = new HashSet<>();

    public static ArrayList<String> history = new ArrayList<>(); // Global History of all messages received

    private Socket socket;

    public ConnectionManager(Socket socket){
        this.socket = socket;
    }


    private synchronized void readInput(Object input){

        if(input.getClass().equals(Message.class)){

            Message message = (Message) input;
            history.add(message.toString());

            writeToRoom(message);

        }
    }

    private synchronized void writeToRoom(Message message){

        activeRooms.get(message.getKey()).forEach(i -> {
            try {
                i.writeObject(message);
                i.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

    }


    public void addToRoom(User user, int room){

        activeRooms.get(room).add(userStreams.get(user)); // Gets the RoomKey from @Param 'room' and adds the OutputStream to the user's client from @Param 'user'

    }


    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());


            activeRooms.get(0).add(out);

            for (;;) {

                Object input = in.readObject();

                if (input == null) {
                    return;
                }

                else if(input.getClass().equals(User.class)){
                    User user = (User) input;
                    userStreams.put(user, out);
                }

                else readInput(input);

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }



        finally {

            try {

                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




}
