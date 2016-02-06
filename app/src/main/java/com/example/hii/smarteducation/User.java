package com.example.hii.smarteducation;

import java.io.Serializable;

/**
 * Created by hii on 1/22/2016.
 */
public class User implements Serializable {
    private String name;

    public User(){

    }
    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    private String imageFile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private String userID;
    private String gender;
    private String passWord;
    private String confirmPassword;

    public User(String name,String email,String userID, String gender, String passWord, String confirmPassword,String imageFile) {
        this.name = name;
        this.email=email;
        this.userID=userID;
        this.gender = gender;
        this.passWord = passWord;
        this.confirmPassword = confirmPassword;
        this.imageFile=imageFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
