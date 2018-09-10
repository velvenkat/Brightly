package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SharedDataModel implements Parcelable{

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    public SharedDataModel(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    protected SharedDataModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<SharedDataModel> CREATOR = new Creator<SharedDataModel>() {
        @Override
        public SharedDataModel createFromParcel(Parcel in) {
            return new SharedDataModel(in);
        }

        @Override
        public SharedDataModel[] newArray(int size) {
            return new SharedDataModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(phone);
    }
}
