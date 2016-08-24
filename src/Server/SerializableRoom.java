package Server;

import objects.User;

import java.io.Serializable;

/**
 * Created by Adam on 8/24/2016.
 */
public class SerializableRoom implements Serializable {


    private int key;             // The room's unique ID
    private String title;        // Title of the Room.
    private String password;     // The password for a password protected room.
    private String description;
    private User owner;

    private boolean isPublic = false;
    private boolean isStatic = false;
    private boolean passwordProtected = false;

    public SerializableRoom(){

    }




    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }
}
