package Server;

import objects.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adam on 8/16/2016.
 */

public class ConnectionManager implements Runnable {

    //private HashMap<Integer, ArrayList<ObjectOutputStream>> activeRooms = new HashMap<>(); // Key is Room ID Number, Value is the Array of ObjectOutputStreams going to clients that are connected to that room.
    private HashMap<Integer, Room> activeRooms = new HashMap<>();

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

        RoomHandler.sendMessage(message);

    }


    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());



            for (;;) {

                Object input = in.readObject();

                if (input == null) {
                    return;
                }

                else if(input.getClass().equals(User.class)){
                    User user = (User) input;
                    ChatUser chatUser = new ChatUser(user, out);
                    RoomHandler.addUser(chatUser, 0);
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
