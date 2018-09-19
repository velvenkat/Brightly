package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChannelListModel implements Parcelable {

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

    @SerializedName("total_set_count")
    @Expose
    private String total_set_count;



    protected ChannelListModel(Parcel in) {
        channel_id = in.readString();
        channel_name = in.readString();
        description = in.readString();
        cover_image = in.readString();
        image_name = in.readString();
        created_by = in.readString();
        created_time = in.readString();
        created_date = in.readString();
        total_set_count = in.readString();
    }

    public static final Creator<ChannelListModel> CREATOR = new Creator<ChannelListModel>() {
        @Override
        public ChannelListModel createFromParcel(Parcel in) {
            return new ChannelListModel(in);
        }

        @Override
        public ChannelListModel[] newArray(int size) {
            return new ChannelListModel[size];
        }
    };

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    @SerializedName("created_by")
    @Expose
    private String created_by;

    @SerializedName("created_time")
    @Expose
    private String created_time;

    @SerializedName("created_date")
    @Expose
    private String created_date;


    public ChannelListModel(String channel_id, String channel_name, String description, String cover_image, String image_name, String total_set_count) {
        this.channel_id = channel_id;
        this.channel_name = channel_name;
        this.description = description;
        this.cover_image = cover_image;
        this.image_name = image_name;
        this.total_set_count = total_set_count;
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

    public String getTotal_set_count() {
        return total_set_count;
    }

    public void setTotal_set_count(String total_set_count) {
        this.total_set_count = total_set_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channel_id);
        dest.writeString(channel_name);
        dest.writeString(description);
        dest.writeString(cover_image);
        dest.writeString(image_name);
        dest.writeString(created_by);
        dest.writeString(created_time);
        dest.writeString(created_date);
        dest.writeString(total_set_count);
    }
}
