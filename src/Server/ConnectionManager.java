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

    public static ArrayList<String> history = new ArrayList<>(); // Global History of all messages received

    private Socket socket;

    private User user;
    private ChatUser chatUser;
    public ConnectionManager(Socket socket){
        this.socket = socket;
    }


    private synchronized void readInput(Object input){


        if(input.getClass().equals(Message.class)){

            Message message = (Message) input;
            int key = message.getKey();
            history.add(message.toString());

            writeToRoom(message, key);

        }

        else if(input.getClass().equals(Command.class)){

            Command command = (Command) input;
            int key = command.getKey();
            history.add(command.toString());

            writeToRoom(command, key);

        }

        else if(input.getClass().equals(ServerRequest.class)){
            receiveRequest((ServerRequest) input);
        }

    }

    private synchronized void receiveRequest(ServerRequest request){

        switch(request.getType()){
            case ROOM_INVITE:

                break;

            case ROOM_REQUEST:
                RoomHandler.createRoom(request.getRoom());
                break;

            case CLOSE_ROOM:

                break;

            case ALERT:

                break;

            case PRIVATE_MESSAGE:

                break;

            default:
                System.out.println("No Case Statement for Server Request Type: " + request.getType());
        }

    }

    private synchronized void writeToRoom(Object object, int key){

        RoomHandler.writeToRoom(object, key);

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
                    user = (User) input;
                    chatUser = new ChatUser(user, out);
                    RoomHandler.addUser(chatUser, 0);

                }

                else readInput(input);

            }

        } catch (IOException | ClassNotFoundException e) {

            RoomHandler.removeUser(chatUser);

            /*
            final ChatUser[] removedUser = new ChatUser[1];
            RoomHandler.getRooms().values().forEach(room -> {
                Room temp = (Room) room;
                temp.getActiveUsers().forEach(u -> {
                   if(u.getUser().getAlias().equals(user.getAlias())) {
                       removedUser[0] = u;
                       return;
                   }
                });
                temp.removeUser(removedUser[0]);
            });
*/
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
