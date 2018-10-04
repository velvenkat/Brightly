package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

public class SetEntryModel implements Parcelable {

 //   String userId;
    String set_id;
    String set_name;
    String card_name = "";
    String card_id;
    String card_description = "";
    String card_multimedia_url = "";
    String image_name = "";
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
        dest.writeString(type);
    }
}
