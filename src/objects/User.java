package objects;

import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * Created by Adam on 8/14/2016.
 */
public class User implements Serializable{

    private User liason;
   // private Image profilePicture;

    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String dob;
    private String sex;
    private String alias;
    private String joined;
    private String permissions;

    public User(){

    }

    public User(String alias){
        this.alias = alias;
    }

    public String getFullName(){
        return this.firstName + " " + this.middleName + " " + this.lastName;
    }


    public User getLiason() {
        return liason;
    }

    public void setLiason(User liason) {
        this.liason = liason;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}

