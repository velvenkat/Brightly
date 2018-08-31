package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationsResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<NotificationsModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NotificationsModel> getData() {
        return data;
    }

    public void setData(List<NotificationsModel> data) {
        this.data = data;
    }
}
