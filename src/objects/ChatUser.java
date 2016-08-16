package objects;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Adam on 8/16/2016.
 */
public class ChatUser implements Runnable {

    private static HashSet<String> names = new HashSet<String>();
    //private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

    private static HashSet<ObjectOutputStream> writers = new HashSet<>();

    private Socket socket;

    public ChatUser(Socket socket) {
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

}
