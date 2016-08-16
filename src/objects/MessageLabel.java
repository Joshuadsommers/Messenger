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
            setScaleY(1.4);
        });

        setOnMouseExited(Event ->{
            setScaleY(1.0);
        });

        setOnMouseClicked(Event ->{
            if(Event.getClickCount() == 2){
                System.out.println("Send Private Message to; " + message.getUser().getAlias());
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
