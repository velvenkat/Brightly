package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SetsListModel {

    @SerializedName("set_id")
    @Expose
    private String set_id;

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

    public SetsListModel(String set_id, String set_name, String description, String thumbnail, String share_link, ArrayList<SharedDataModel> shared_data, boolean isDelSel) {
        this.set_id = set_id;
        this.set_name = set_name;
        this.description = description;
        this.thumbnail = thumbnail;
        this.share_link = share_link;
        this.shared_data = shared_data;
        this.isDelSel = isDelSel;
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

    public ArrayList<SharedDataModel> getShared_data() {
        return shared_data;
    }

    public void setShared_data(ArrayList<SharedDataModel> shared_data) {
        this.shared_data = shared_data;
    }
}
