package chat;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created by Adam on 8/18/2016.
 */
public class FontLabel extends Label {

    private Color color;
    private String colorName;
    private String family;

    public FontLabel(Color color, String name){
        this.color = color;
        this.colorName = name;
        setText(name);
        setTextFill(color);
        createListeners();
    }

    public FontLabel(String family){
        this.family = family;
        setFont(new Font(family, 14));
        setText(family);
        createListeners();
    }

    private void createListeners(){
        setOnMouseEntered(Event ->{
            setScaleY(1.3);
        });
        setOnMouseExited(Event ->{
            setScaleY(1.0);
        });

    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
