package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardsListModel implements Parcelable {

    @SerializedName("image_id")
    @Expose
    private String image_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("image_name")
    @Expose
    private String image_name;

    public CardsListModel(){

    }
    public CardsListModel(String image_id, String title, String imgUrl, String description, String image_name) {
        this.image_id = image_id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.description = description;
        this.image_name = image_name;
    }

    protected CardsListModel(Parcel in) {
        image_id = in.readString();
        title = in.readString();
        imgUrl = in.readString();
        description = in.readString();
        image_name = in.readString();
    }

    public static final Creator<CardsListModel> CREATOR = new Creator<CardsListModel>() {
        @Override
        public CardsListModel createFromParcel(Parcel in) {
            return new CardsListModel(in);
        }

        @Override
        public CardsListModel[] newArray(int size) {
            return new CardsListModel[size];
        }
    };

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_id);
        parcel.writeString(title);
        parcel.writeString(imgUrl);
        parcel.writeString(description);
        parcel.writeString(image_name);
    }
}