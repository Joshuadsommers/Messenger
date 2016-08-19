package objects;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Adam on 8/19/2016.
 */
public class ErrorLabel extends Label {

    private String message;
    private String timestamp;

    public ErrorLabel(String message){
        this.message = message;
        this.timestamp = MasterClass.timeStamp();

        createTooltip();

        setBlendMode(BlendMode.EXCLUSION);
        setFont(new Font("Georgia", 16));
        setTextFill(Color.RED);
        setText(message);


    }

    private void createTooltip(){

        String tooltipText = "Time: " + timestamp + "\n" +
                "Message: " + message;
        setTooltip(new Tooltip(tooltipText));
    }
}
