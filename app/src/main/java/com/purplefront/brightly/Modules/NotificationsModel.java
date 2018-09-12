package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsModel implements Parcelable {

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date_time")
    @Expose
    private String date_time;

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("channel_id")
    @Expose
    private String channel_id;

    @SerializedName("shared_user_profile_pic")
    @Expose
    private String shared_user_profile_pic;

    @SerializedName("set_details")
    @Expose
    private NotificationsSetDetail notificationsSetDetail;

    public NotificationsModel(String content, String date_time, String action, String channel_id, String shared_user_profile_pic, NotificationsSetDetail notificationsSetDetail) {
        this.content = content;
        this.date_time = date_time;
        this.action = action;
        this.channel_id = channel_id;
        this.shared_user_profile_pic = shared_user_profile_pic;
        this.notificationsSetDetail = notificationsSetDetail;
    }

    protected NotificationsModel(Parcel in) {
        content = in.readString();
        date_time = in.readString();
        action = in.readString();
        channel_id = in.readString();
        shared_user_profile_pic = in.readString();
        notificationsSetDetail = in.readParcelable(NotificationsSetDetail.class.getClassLoader());
    }

    public static final Creator<NotificationsModel> CREATOR = new Creator<NotificationsModel>() {
        @Override
        public NotificationsModel createFromParcel(Parcel in) {
            return new NotificationsModel(in);
        }

        @Override
        public NotificationsModel[] newArray(int size) {
            return new NotificationsModel[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getShared_user_profile_pic() {
        return shared_user_profile_pic;
    }

    public void setShared_user_profile_pic(String shared_user_profile_pic) {
        this.shared_user_profile_pic = shared_user_profile_pic;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public NotificationsSetDetail getNotificationsSetDetail() {
        return notificationsSetDetail;
    }

    public void setNotificationsSetDetail(NotificationsSetDetail notificationsSetDetail) {
        this.notificationsSetDetail = notificationsSetDetail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(date_time);
        dest.writeString(action);
        dest.writeString(channel_id);
        dest.writeString(shared_user_profile_pic);
        dest.writeParcelable(notificationsSetDetail, flags);
    }
}
