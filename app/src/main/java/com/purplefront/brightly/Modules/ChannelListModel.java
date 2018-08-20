package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelListModel {

    @SerializedName("channel_id")
    @Expose
    private String channel_id;

    @SerializedName("name")
    @Expose
    private String channel_name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("cover_image")
    @Expose
    private String cover_image;

    @SerializedName("image_name")
    @Expose
    private String image_name;

    public ChannelListModel(String channel_id, String channel_name, String description, String cover_image, String image_name) {
        this.channel_id = channel_id;
        this.channel_name = channel_name;
        this.description = description;
        this.cover_image = cover_image;
        this.image_name = image_name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
