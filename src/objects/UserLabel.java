package objects;

import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Adam on 8/19/2016.
 */
public class UserLabel extends Label {

    private User user;
    ImageView iconImage = new ImageView("Images/online_icon.png");

    public UserLabel(User user){
        this.user = user;
        setFonts();
        createListeners();
        setImage();

    }

    private void setImage(){
        iconImage.setFitWidth(20);
        iconImage.setFitHeight(20);
        setGraphic(iconImage);

    }

    private void setFonts(){
        setFont(new Font("Georgia", 14));
        setTextFill(Color.FORESTGREEN);
        setBlendMode(BlendMode.EXCLUSION);
        setText(user.getAlias());

    }

    private void createListeners(){
        setOnMouseEntered(Event ->{
            setScaleY(1.2);
        });

        setOnMouseExited(Event ->{
            setScaleY(1);
        });

        setOnMouseClicked(Event ->{
            if(Event.getClickCount() == 2){
                System.out.println("PM TO: " + user.getAlias());
            }
        });


        setTooltip(new Tooltip(user.toString()));
    }


    public User getUser() {
        return user;
    }
}
