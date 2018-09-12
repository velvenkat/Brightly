package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationsModel {

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
}
