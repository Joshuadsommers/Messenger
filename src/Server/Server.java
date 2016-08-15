package Server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Josh on 8/15/2016.
 */
public class Server {

    private static int PORT = 6969;
    private ServerSocket server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;


    public static void main(String[] args) {
        Server s = new Server();
        s.startRunning();
    }
    public void startRunning() {

        try {
            server = new ServerSocket(PORT);
            while(true) {
                try {
                    // connect here
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }catch(EOFException eof) {
                    System.out.println("Connection ended due to EOF exception");
                }finally {
                    exitCleanup();
                }
            }
        } catch(IOException io) {
            io.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        connection = server.accept();
    }

    // Sets up IO streams for communication
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();

        input = new ObjectInputStream(connection.getInputStream());
    }

    // Conversation started by this point
    private void whileChatting() {
        Object message;
        boolean chatting = true;
        do {
            try {
                message = input.readObject();
            } catch(ClassNotFoundException cnfe) {
                System.out.println("Received message is not of a recognized class.");
                chatting = false;
            } catch(IOException io) {
                System.out.println("IO exception when receiving message from client.");
                chatting = false;
            }
        } while(chatting);
    }

    private void exitCleanup() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch(IOException io) {
            io.printStackTrace();
            System.out.println("Problem closing sockets/connection.");
        }
    }

    private void sendToRoom(Room r, MessageToServer m) {
        try {

        } catch(IOException io) {
            System.out.println("Unable to send message to room.");
        }
    }

}