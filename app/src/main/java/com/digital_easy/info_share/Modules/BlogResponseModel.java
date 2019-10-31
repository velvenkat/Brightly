package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlogResponseModel implements Parcelable {
    public BlogResponseModel() {

    }

    protected BlogResponseModel(Parcel in) {
        values = in.readString();
        type = in.readString();
        contactHelperModule = in.readParcelable(ContactHelperModule.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(values);
        dest.writeString(type);
        dest.writeParcelable(contactHelperModule, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BlogResponseModel> CREATOR = new Creator<BlogResponseModel>() {
        @Override
        public BlogResponseModel createFromParcel(Parcel in) {
            return new BlogResponseModel(in);
        }

        @Override
        public BlogResponseModel[] newArray(int size) {
            return new BlogResponseModel[size];
        }
    };

    public String getValue() {
        return values;
    }

    public void setValue(String value) {
        this.values = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContactHelperModule getContactHelperModule() {
        return contactHelperModule;
    }

    public void setContactHelperModule(ContactHelperModule contactHelperModule) {
        this.contactHelperModule = contactHelperModule;
    }

    @SerializedName("value")
    @Expose
    private String values;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("contact")
    @Expose
    private ContactHelperModule contactHelperModule;

}
