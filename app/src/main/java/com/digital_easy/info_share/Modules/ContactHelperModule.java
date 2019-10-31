package com.digital_easy.info_share.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactHelperModule implements Parcelable {
    public ContactHelperModule(){

    }
    @SerializedName("value")
    @Expose
    public String values;
    @SerializedName("type")
    @Expose
    public String type;

    protected ContactHelperModule(Parcel in) {
        values = in.readString();
        type = in.readString();
    }

    public static final Creator<ContactHelperModule> CREATOR = new Creator<ContactHelperModule>() {
        @Override
        public ContactHelperModule createFromParcel(Parcel in) {
            return new ContactHelperModule(in);
        }

        @Override
        public ContactHelperModule[] newArray(int size) {
            return new ContactHelperModule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(values);
        dest.writeString(type);
    }
}
