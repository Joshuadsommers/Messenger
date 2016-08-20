package objects;

import enums.InformationType;

import java.io.Serializable;

/**
<<<<<<< HEAD
 * Created by Josh on 8/15/2016.
=======
 * Created by Adam on 8/14/2016.
>>>>>>> origin/master
 */
public class Message implements Serializable {




    private User user;

    private int key;
    private String font;
    private String fontColor;
    private boolean timestamp;

    private String rawText;
    private String timeStamp;
    private String message;

    private double r;
    private double g;
    private double b;

    public Message(int key, User user, String rawText, String font, double r, double g, double b, boolean timestamp){
        this.key = key;
        this.user = user;
        this.rawText = rawText;
        this.font = font;
        this.r = r;
        this.g = g;
        this.b = b;
        this.timestamp = timestamp;

        this.timeStamp = timeStamp();

        format();

    }

    public Message(int key, String rawText, InformationType type){
        this.key = key;
        this.rawText = rawText;
        this.timeStamp = timeStamp();

        format();

    }

    public String getRawText(){
        return this.rawText;
    }

    public User getUser(){
        return this.user;
    }

    private void format(){
        this.message = timestamp ? timeStamp() + "[" + user.getAlias() + "] - " + rawText : "[" + user.getAlias() + "] - " + rawText;
    }

    private String timeStamp(){
        return MasterClass.timeStamp();
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public int getKey() {
        return key;
    }

    public String toString(){
        return "<" + key + ">" + getMessage();
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }
}
