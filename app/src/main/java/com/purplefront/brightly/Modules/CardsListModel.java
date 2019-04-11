package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsListModel implements Parcelable {

    @SerializedName("card_id")
    @Expose
    private String card_id;

    @SerializedName("title")
    @Expose
    private String title;

    public List<MultipleImageModel> getMultiple_img_url() {
        return multiple_img_url;
    }

    public void setMultiple_img_url(List<MultipleImageModel> multiple_img_url) {
        this.multiple_img_url = multiple_img_url;
    }

    @SerializedName("multiple_images")
    @Expose
    private List<MultipleImageModel> multiple_img_url;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("name")
    @Expose
    private String name;

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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @SerializedName("created_by")
    @Expose
    private String created_by;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @SerializedName("comment")
    @Expose
    private String comment;


    @SerializedName("type")
    @Expose
    private String type;
    private boolean isDelSel = false;

    public boolean isDelSel() {
        return isDelSel;
    }

    public void setDelSel(boolean delSel) {
        isDelSel = delSel;
    }

    public CardsListModel() {

    }

    public CardsListModel(String card_id, String title, String url, String description, String name, String type) {
        this.card_id = card_id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.name = name;
        this.type = type;
    }

    protected CardsListModel(Parcel in) {
        card_id = in.readString();
        title = in.readString();
        url = in.readString();
        description = in.readString();
        name = in.readString();
        type = in.readString();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(card_id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(created_by);
        dest.writeString(comment);
        dest.writeString(type);
        dest.writeByte((byte) (isDelSel ? 1 : 0));
    }
}