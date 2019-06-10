package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SetEntryModel implements Parcelable {

    //   String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_id;
    String card_description = "";
    String card_multimedia_url = "";
    String image_name = "";


    private String contact_name;

    public static final Creator<SetEntryModel> CREATOR = new Creator<SetEntryModel>() {
        @Override
        public SetEntryModel createFromParcel(Parcel in) {
            return new SetEntryModel(in);
        }

        @Override
        public SetEntryModel[] newArray(int size) {
            return new SetEntryModel[size];
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

    private String contact_email;

    private String contact_cell_phone;

    private String contact_office_phone;

    private String contact_company;


    public List<MultipleImageModel> getMultipleImageModelList() {
        return multipleImageModelList;
    }

    public void setMultipleImageModelList(List<MultipleImageModel> multipleImageModelList) {
        this.multipleImageModelList = multipleImageModelList;
    }

    List<MultipleImageModel> multipleImageModelList;
    String type = "";

    public SetEntryModel() {
    }

    protected SetEntryModel(Parcel in) {
        set_id = in.readString();
        set_name = in.readString();
        card_name = in.readString();
        card_id = in.readString();
        card_description = in.readString();
        card_multimedia_url = in.readString();
        image_name = in.readString();
        type = in.readString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /* public String getUserId() {
         return userId;
     }

     public void setUserId(String userId) {
         this.userId = userId;
     }
 */
    public String getSet_id() {
        return set_id;
    }

    public void setSet_id(String set_id) {
        this.set_id = set_id;
    }

    public String getSet_name() {
        return set_name;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCard_description() {
        return card_description;
    }

    public void setCard_description(String card_description) {
        this.card_description = card_description;
    }

    public String getCard_multimedia_url() {
        return card_multimedia_url;
    }

    public void setCard_multimedia_url(String card_multimedia_url) {
        this.card_multimedia_url = card_multimedia_url;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(set_id);
        dest.writeString(set_name);
        dest.writeString(card_name);
        dest.writeString(card_id);
        dest.writeString(card_description);
        dest.writeString(card_multimedia_url);
        dest.writeString(image_name);
        dest.writeString(contact_name);
        dest.writeString(contact_email);
        dest.writeString(contact_cell_phone);
        dest.writeString(contact_office_phone);
        dest.writeString(contact_company);
        dest.writeString(type);
    }
}
