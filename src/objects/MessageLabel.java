package objects;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

/**
 * Created by Adam on 8/14/2016.
 */
public class MessageLabel extends Label {

    private Message message;

    public MessageLabel(Message message){
        this.message = message;
        createTooltip();
        createListeners();

        setText(message.getMessage());


    }

    private void createListeners(){
        setOnMouseEntered(Event ->{
<<<<<<< HEAD
            setScaleY(1.4); // On mouse hover, we make the message bigger as to express that the message is selected and can be double clicked
        });

        setOnMouseExited(Event ->{
            setScaleY(1.0); // Once the mouse is exited, we return the label to the normal size.
=======
            setScaleY(1.4);
        });

        setOnMouseExited(Event ->{
            setScaleY(1.0);
>>>>>>> origin/master
        });

        setOnMouseClicked(Event ->{
            if(Event.getClickCount() == 2){
<<<<<<< HEAD
                System.out.println("Send Private Message to: " + message.getUser().getAlias()); // can hardcode functionality to private message the sender of the message the label is displaying
=======
                System.out.println("Send Private Message to; " + message.getUser().getAlias());
>>>>>>> origin/master
            }
        });

    }

    private void createTooltip(){

        String tooltipText = "Time: " + message.getTimeStamp() + "\n" +
                             "User: " + message.getUser().getAlias() + "\n" +
                             "Message: " + message.getRawText();

        setTooltip(new Tooltip(tooltipText));
    }



}
