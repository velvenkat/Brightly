package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelListResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("channels")
    @Expose
    private List<ChannelListModel> channels = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChannelListModel> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelListModel> channels) {
        this.channels = channels;
    }
}
