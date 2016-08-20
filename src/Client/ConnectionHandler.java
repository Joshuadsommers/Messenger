package Client;

import Server.InformationMessage;
import enums.CommandHandler;
import enums.InformationType;
import objects.Command;
import objects.MasterClass;
import objects.Message;
import objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Adam on 4/11/2016.
 */

public class ConnectionHandler implements Runnable {

    boolean isConnected = false;

    Socket socket;
    private User user;
    TerminalController terminal;

    private static ObjectInputStream input;
    private static ObjectOutputStream output;

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

        MasterClass.client.append(new InformationMessage("You have joined Global chat.", 0));

        sendMessage(user);

        while(true){

            try {

                Object rawInput = input.readObject();



                if(rawInput.getClass().equals(Message.class)){

                    Message message = (Message) rawInput;

                    terminal.appendMessage(message);

                }

                else if(rawInput.getClass().equals(Command.class)){

                    Command command = (Command) rawInput;

                    CommandHandler.receiveCommand(command);

                }

                else if(rawInput.getClass().equals(InformationMessage.class)){
                    InformationMessage message = (InformationMessage) rawInput;

                    MasterClass.client.receiveInformationMessage(message);
                }

                else if(rawInput.getClass().equals(HashSet.class)) {
                    System.out.println("Hits Connection Handler");
                    MasterClass.client.updateList( (HashSet) rawInput);
                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    public static void sendCommand(Command command){

        try {
            output.writeObject(command);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
