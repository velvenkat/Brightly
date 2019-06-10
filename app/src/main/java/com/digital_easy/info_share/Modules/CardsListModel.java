package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsListModel implements Parcelable {

    //Contact
    @SerializedName("contact_name")
    @Expose
    private String contact_name;

    @SerializedName("contact_email")
    @Expose
    private String contact_email;

    @SerializedName("contact_cell_phone")
    @Expose
    private String contact_cell_phone;

    @SerializedName("contact_office_phone")
    @Expose
    private String contact_office_phone;

    @SerializedName("contact_company")
    @Expose
    private String contact_company;


    protected CardsListModel(Parcel in) {
        contact_name = in.readString();
        contact_email = in.readString();
        contact_cell_phone = in.readString();
        contact_office_phone = in.readString();
        contact_company = in.readString();

        card_id = in.readString();
        title = in.readString();
        url = in.readString();
        description = in.readString();
        name = in.readString();
        created_by = in.readString();
        comment = in.readString();
        type = in.readString();
        isDelSel = in.readByte() != 0;
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

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getContact_cell_phone() {
        return contact_cell_phone;
    }

    public void setContact_cell_phone(String contact_cell_phone) {
        this.contact_cell_phone = contact_cell_phone;
    }

    public String getContact_office_phone() {
        return contact_office_phone;
    }

    public void setContact_office_phone(String contact_office_phone) {
        this.contact_office_phone = contact_office_phone;
    }

    public String getContact_company() {
        return contact_company;
    }

    public void setContact_company(String contact_company) {
        this.contact_company = contact_company;
    }


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
        dest.writeString(contact_name);
        dest.writeString(contact_email);
        dest.writeString(contact_cell_phone);
        dest.writeString(contact_office_phone);
        dest.writeString(contact_company);
 
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