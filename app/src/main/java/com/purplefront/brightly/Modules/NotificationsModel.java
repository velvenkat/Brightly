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

    @SerializedName("shared_user_profile_pic")
    @Expose
    private String shared_user_profile_pic;


    public NotificationsModel(String content, String date_time, String shared_user_profile_pic) {
        this.content = content;
        this.date_time = date_time;
        this.shared_user_profile_pic = shared_user_profile_pic;
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
}
