package objects;

import Server.InformationMessage;
import enums.InformationType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Adam on 8/19/2016.
 */
public class InformationLabel extends Label  {

    private String message;
    private String timestamp;
    private int key;
    private User user;
    private InformationMessage informationMessage;
    private InformationType type;

    public InformationLabel(String message){
        this.message = message;
        this.timestamp = MasterClass.timeStamp();
        type = InformationType.ERROR;
        setBlendMode(BlendMode.EXCLUSION);
        setFont(new Font("Georgia", 16));
        setTextFill(Color.RED);
        setText(message);
        createTooltip();
        setAlignment(Pos.CENTER);


    }

    public InformationLabel(InformationMessage message){
        this.informationMessage = message;
        this.message = message.getText();
        this.timestamp = message.getTimestamp();
        type = informationMessage.getType();
        setBlendMode(BlendMode.EXCLUSION);
        setFont(new Font("Georgia", 16));
        setFontColor();

        setText(message.getText());
        polymorph();
        createTooltip();

    }

    private void setFontColor(){
        switch(type){
            default:
            case ROOM_JOINED:
            case ROOM_CREATED:
            case USER_JOINED:
                setTextFill(Color.FORESTGREEN);
                break;

            case ERROR:
            case USER_LEFT:
                setTextFill(Color.RED);
                break;

            case INFORMATION:
                setTextFill(Color.AZURE);
                break;

        }
    }

    public InformationLabel(InformationMessage message, Color color){
        this.informationMessage = message;
        this.message = message.getText();
        this.timestamp = message.getTimestamp();
        type = informationMessage.getType();
        setBlendMode(BlendMode.EXCLUSION);
        setFont(new Font("Georgia", 16));
        setTextFill(color);
        setText(message.getText());
        polymorph();
        createTooltip();
    }

    private void polymorph(){

        switch(type){
            case INFORMATION:


                break;

            case USER_JOINED:
                this.message = informationMessage.getText();
                this.user = informationMessage.getUser();
                this.key = informationMessage.getKey();
                this.timestamp = informationMessage.getTimestamp();

                break;

        }
    }





    private void createTooltip(){

        String tooltipText = "";
        switch(type){
            case INFORMATION:

                 tooltipText = "Time: " + timestamp + "\n" +
                        "Message: [" + message + "]";
                break;

            case USER_JOINED:
                tooltipText = "Time: " + timestamp + "\n" +
                        "User: [" + user.getAlias() + "]" + "\n" +
                        "Room Key: [" + key + "]" + "\n" +
                        "Message Type: [" + type + "]";

                break;

        }


        setTooltip(new Tooltip(tooltipText));
    }

    public int getKey() {
        return key;
    }



}
