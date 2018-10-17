package com.purplefront.brightly.Modules;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SetsListModel implements Parcelable{

    @SerializedName("set_id")
    @Expose
    private String set_id;

    protected SetsListModel(Parcel in) {
        set_id = in.readString();
        share_access = in.readString();
        set_name = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        share_link = in.readString();
        total_card_count = in.readString();
        shared_by = in.readString();
        shared_data = in.createTypedArrayList(SharedDataModel.CREATOR);
        isDelSel = in.readByte() != 0;
    }

    public static final Creator<SetsListModel> CREATOR = new Creator<SetsListModel>() {
        @Override
        public SetsListModel createFromParcel(Parcel in) {
            return new SetsListModel(in);
        }

        @Override
        public SetsListModel[] newArray(int size) {
            return new SetsListModel[size];
        }
    };

    public String getShare_access() {
        return share_access;
    }

    public void setShare_access(String share_access) {
        this.share_access = share_access;
    }

    @SerializedName("share_access")
    @Expose
    private String share_access;

    @SerializedName("name")
    @Expose
    private String set_name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("share_link")
    @Expose
    private String share_link;

    @SerializedName("total_card_count")
    @Expose
    private String total_card_count;

    @SerializedName("shared_by")
    @Expose
    private String shared_by;

    @SerializedName("shared_data")
    @Expose
    private ArrayList<SharedDataModel> shared_data = null;


    public boolean isDelSel() {
        return isDelSel;
    }

    public void setDelSel(boolean delSel) {
        isDelSel = delSel;
    }

    private boolean isDelSel=false;

    public SetsListModel(String set_id, String set_name, String description, String thumbnail, String share_link, ArrayList<SharedDataModel> shared_data, boolean isDelSel, String total_card_count, String shared_by,String shareAccess) {
        this.set_id = set_id;
        this.set_name = set_name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.share_link = share_link;
        this.shared_data = shared_data;
        this.isDelSel = isDelSel;
        this.total_card_count = total_card_count;
        this.shared_by = shared_by;
        this.share_access=shareAccess;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getShare_link() {
        return share_link;
    }

    public void setShare_link(String share_link) {
        this.share_link = share_link;
    }

    public String getTotal_card_count() {
        return total_card_count;
    }

    public void setTotal_card_count(String total_card_count) {
        this.total_card_count = total_card_count;
    }

    public String getShared_by() {
        return shared_by;
    }

    public void setShared_by(String shared_by) {
        this.shared_by = shared_by;
    }

    public ArrayList<SharedDataModel> getShared_data() {
        return shared_data;
    }

    public void setShared_data(ArrayList<SharedDataModel> shared_data) {
        this.shared_data = shared_data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(set_id);
        dest.writeString(share_access);
        dest.writeString(set_name);
        dest.writeString(description);
        dest.writeString(thumbnail);
        dest.writeString(share_link);
        dest.writeString(total_card_count);
        dest.writeString(shared_by);
        dest.writeTypedList(shared_data);
        dest.writeByte((byte) (isDelSel ? 1 : 0));
    }
}
