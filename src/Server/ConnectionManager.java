package Server;

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

    public static ArrayList<User> activeUsers = new ArrayList<>(); // Global Array of all Active Users
    public static ArrayList<User> visibleUsers = new ArrayList<>(); // Users that are visible as online.

    public static ArrayList<String> history = new ArrayList<>(); // Global History of all messages received
    public static HashSet<ObjectOutputStream> globalOutput = new HashSet<>();


    private Socket socket;

    public ConnectionManager(Socket socket){
        this.socket = socket;
    }

    public void requestRoom(ServerRequest request){

    }

    public void createRoom(){

    }

    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            globalOutput.add(out);

            for (;;) {
                //String input = in.readLine();
                Object input = in.readObject();

                if (input == null) {
                    return;
                }

                globalOutput.forEach(i -> {
                    //String message = input;//"server: ") + input;
                    try {
                        i.writeObject(input);
                        i.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }



                });

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
