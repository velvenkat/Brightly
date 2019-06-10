package com.digital_easy.info_share.Modules;

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

    @SerializedName("shared_by")
    @Expose
    private String shared_by;

    @SerializedName("share_access")
    @Expose
    private String share_access;

    @SerializedName("installed")
    @Expose
    private String instlled;


    public SharedDataModel(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    protected SharedDataModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        phone = in.readString();
        shared_by = in.readString();
        instlled = in.readString();
        share_access = in.readString();

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

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    public String getInstlled() {
        return instlled;
    }

    public void setInstlled(String instlled) {
        this.instlled = instlled;
    }

    public String getShare_access() {
        return share_access;
    }

    public void setShare_access(String share_access) {
        this.share_access = share_access;
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
        parcel.writeString(shared_by);
        parcel.writeString(instlled);
        parcel.writeString(share_access);
    }
}
