package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentsListResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<CommentsModel> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CommentsModel> getData() {
        return data;
    }

    public void setData(List<CommentsModel> data) {
        this.data = data;
    }
}
