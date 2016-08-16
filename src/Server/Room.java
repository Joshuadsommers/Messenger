package Server;

import objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Josh on 8/15/2016.
 */
public class Room implements Runnable {

    // Room is created from server with all InetAddresses as param
    // When users are added to a room after room is created they will use addUser() and we need to append to addresses

    private HashSet<User> users = new HashSet<>();
    //private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private HashSet<ObjectOutputStream> writers = new HashSet<>();

    private Socket socket;

    public Room(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            writers.add(out);

            for (;;) {
                //String input = in.readLine();
                Object input = in.readObject();

                if (input == null) {
                    return;
                }

                writers.forEach(i -> {
                    //String message = input;//"server: ") + input;
                    try {
                        i.writeObject(input);
                        i.flush();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }



                });

            }

        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }

    }
    public Room(InetAddress... addresses){
        for( InetAddress a : addresses) {
            // send message obj to each client in room
        }
    }

    public void addUser(InetAddress a) {
        // Add user to addresses
    }

    public void removeUser(InetAddress a) {
        // Remove user from addresses
    }
}
