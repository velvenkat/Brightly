package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsSetDetail implements Parcelable{

    @SerializedName("set_id")
    @Expose
    private String set_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created_by")
    @Expose
    private String created_by;

    @SerializedName("shared_by")
    @Expose
    private String shared_by;


    public NotificationsSetDetail(String set_id, String name, String description, String created_by) {
        this.set_id = set_id;
        this.name = name;
        this.description = description;
        this.created_by = created_by;
    }

    protected NotificationsSetDetail(Parcel in) {
        set_id = in.readString();
        name = in.readString();
        description = in.readString();
        created_by = in.readString();
        shared_by = in.readString();
    }

    public static final Creator<NotificationsSetDetail> CREATOR = new Creator<NotificationsSetDetail>() {
        @Override
        public NotificationsSetDetail createFromParcel(Parcel in) {
            return new NotificationsSetDetail(in);
        }

        @Override
        public NotificationsSetDetail[] newArray(int size) {
            return new NotificationsSetDetail[size];
        }
    };

    public String getSet_id() {
        return set_id;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(set_id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(created_by);
        parcel.writeString(shared_by);
    }
}
