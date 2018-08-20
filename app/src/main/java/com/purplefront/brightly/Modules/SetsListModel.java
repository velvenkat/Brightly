package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public SetsListModel(String set_id, String set_name, String description, String thumbnail) {
        this.set_id = set_id;
        this.set_name = set_name;
        this.description = description;
        this.thumbnail = thumbnail;
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
}
