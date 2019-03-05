package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralVarModel implements Parcelable {
    @SerializedName("section")
    @Expose
    private String section;

    protected GeneralVarModel(Parcel in) {
        section = in.readString();
        fetch_key = in.readString();
        singular = in.readString();
        plural = in.readString();
        similar_row_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeString(fetch_key);
        dest.writeString(singular);
        dest.writeString(plural);
        dest.writeString(similar_row_count);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeneralVarModel> CREATOR = new Creator<GeneralVarModel>() {
        @Override
        public GeneralVarModel createFromParcel(Parcel in) {
            return new GeneralVarModel(in);
        }

        @Override
        public GeneralVarModel[] newArray(int size) {
            return new GeneralVarModel[size];
        }
    };

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getFetch_key() {
        return fetch_key;
    }

    public void setFetch_key(String fetch_key) {
        this.fetch_key = fetch_key;
    }

    public String getSingular() {
        return singular;
    }

    public void setSingular(String singular) {
        this.singular = singular;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getSimilar_row_count() {
        return similar_row_count;
    }

    public void setSimilar_row_count(String similar_row_count) {
        this.similar_row_count = similar_row_count;
    }

    @SerializedName("fetch_key")
    @Expose
    private String fetch_key;

    @SerializedName("singular")
    @Expose
    private String singular;

    @SerializedName("plural")
    @Expose
    private String plural;

    @SerializedName("similar_row_count")
    @Expose
    private String similar_row_count;


}
