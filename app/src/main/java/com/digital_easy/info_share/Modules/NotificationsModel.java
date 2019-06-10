package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsModel implements Parcelable {

    @SerializedName("type")
    @Expose
    private String type;

    protected NotificationsModel(Parcel in) {
        type = in.readString();
        card_created_by = in.readString();
        card_id = in.readString();
        created_by = in.readString();
        content = in.readString();
        date_time = in.readString();
        action = in.readString();
        channel_id = in.readString();
        channel_name = in.readString();
        badge = in.readString();
        shared_user_profile_pic = in.readString();
        card_order_position = in.readString();
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

    public String getCard_created_by() {
        return card_created_by;
    }

    public void setCard_created_by(String card_created_by) {
        this.card_created_by = card_created_by;
    }

    @SerializedName("card_created_by")
    @Expose
    private String card_created_by;

    @SerializedName("card_id")
    @Expose
    private String card_id;


    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @SerializedName("created_by")
    @Expose
    private String created_by;


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

    @SerializedName("channel_name")
    @Expose
    private String channel_name;

    @SerializedName("badge")
    @Expose
    private String badge;

    @SerializedName("shared_user_profile_pic")
    @Expose
    private String shared_user_profile_pic;

    @SerializedName("card_order_position")
    @Expose
    private String card_order_position;

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


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getCard_order_position() {
        return card_order_position;
    }

    public void setCard_order_position(String card_order_position) {
        this.card_order_position = card_order_position;
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

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
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
        dest.writeString(type);
        dest.writeString(card_created_by);
        dest.writeString(card_id);
        dest.writeString(created_by);
        dest.writeString(content);
        dest.writeString(date_time);
        dest.writeString(action);
        dest.writeString(channel_id);
        dest.writeString(channel_name);
        dest.writeString(badge);
        dest.writeString(shared_user_profile_pic);
        dest.writeString(card_order_position);
        dest.writeParcelable(notificationsSetDetail, flags);
    }
}
