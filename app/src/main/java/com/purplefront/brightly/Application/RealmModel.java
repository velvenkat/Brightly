package com.purplefront.brightly.Application;

import io.realm.RealmObject;

public class RealmModel extends RealmObject {

    private String deviceToken;

    private String User_Id;

    private String User_Name;

    private String User_Email;

    private String User_PhoneNumber;

    private String User_CompanyName;

    private String image;

    private String image_name;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getUser_PhoneNumber() {
        return User_PhoneNumber;
    }

    public void setUser_PhoneNumber(String user_PhoneNumber) {
        User_PhoneNumber = user_PhoneNumber;
    }

    public String getUser_CompanyName() {
        return User_CompanyName;
    }

    public void setUser_CompanyName(String user_CompanyName) {
        User_CompanyName = user_CompanyName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
