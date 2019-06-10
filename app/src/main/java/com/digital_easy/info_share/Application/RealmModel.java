package com.digital_easy.info_share.Application;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class RealmModel extends RealmObject implements Parcelable{

    private String deviceToken;

    private String User_Id;

    private String User_Name;

    private String User_Email;

    private String User_PhoneNumber;

    private String User_CompanyName;

    private String image;

    private String image_name;

    protected RealmModel(Parcel in) {
        deviceToken = in.readString();
        User_Id = in.readString();
        User_Name = in.readString();
        User_Email = in.readString();
        User_PhoneNumber = in.readString();
        User_CompanyName = in.readString();
        image = in.readString();
        image_name = in.readString();
    }
   public  RealmModel()
   {

   }
    public static final Creator<RealmModel> CREATOR = new Creator<RealmModel>() {
        @Override
        public RealmModel createFromParcel(Parcel in) {
            return new RealmModel(in);
        }

        @Override
        public RealmModel[] newArray(int size) {
            return new RealmModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceToken);
        dest.writeString(User_Id);
        dest.writeString(User_Name);
        dest.writeString(User_Email);
        dest.writeString(User_PhoneNumber);
        dest.writeString(User_CompanyName);
        dest.writeString(image);
        dest.writeString(image_name);
    }
}
