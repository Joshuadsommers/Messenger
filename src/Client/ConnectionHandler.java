package Client;

import objects.Command;
import objects.Message;
import objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Adam on 4/11/2016.
 */

public class ConnectionHandler implements Runnable {

    boolean isConnected = false;

    Socket socket;
    private User user;
    TerminalController terminal;

    ObjectInputStream input;
    ObjectOutputStream output;

    public ConnectionHandler(User user, TerminalController terminal){

        this.user = user;
        this.terminal = terminal;

    }

    public void connect(){
        while(!isConnected){
            try {
                String IP = "127.0.0.1";           //"52.43.163.58";
                socket = new Socket(IP, 6969);

                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());
                isConnected = true;

                whileConnected();

            } catch (IOException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                connect();
            }
        }

    }

    private void whileConnected(){
        isConnected = true;

        sendMessage(user);

        while(true){

            try {

                Object rawInput = input.readObject();

                if(rawInput.getClass().equals(Message.class)){

                    Message message = (Message) rawInput;

                    terminal.appendMessage(message);

                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sendCommand(Command command){

    }

    public void sendMessage(Object message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                connect();
            }
        };
        thread.start();
    }
}
