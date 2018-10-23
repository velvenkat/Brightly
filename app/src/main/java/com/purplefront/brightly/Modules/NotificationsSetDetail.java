package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsSetDetail implements Parcelable {

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

    @SerializedName("share_access")
    @Expose
    private String share_access;

    @SerializedName("share_link")
    @Expose
    private String share_link;


    protected NotificationsSetDetail(Parcel in) {
        set_id = in.readString();
        name = in.readString();
        description = in.readString();
        created_by = in.readString();
        shared_by = in.readString();
        share_access = in.readString();
        share_link = in.readString();
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

    public String getShare_access() {
        return share_access;
    }

    public void setShare_access(String share_access) {
        this.share_access = share_access;
    }

    public NotificationsSetDetail(String set_id, String name, String description, String created_by) {
        this.set_id = set_id;
        this.name = name;
        this.description = description;
        this.created_by = created_by;
    }

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

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(set_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(created_by);
        dest.writeString(shared_by);
        dest.writeString(share_access);
        dest.writeString(share_link);
    }
}
