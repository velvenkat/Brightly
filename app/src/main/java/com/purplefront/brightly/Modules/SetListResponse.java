package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SetListResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("sets")
    @Expose
    private ArrayList<SetsListModel> sets = null;

    public SetsListModel getSet_obj() {
        return set_obj;
    }

    public void setSet_obj(SetsListModel set_obj) {
        this.set_obj = set_obj;
    }

    @SerializedName("set_details")
    @Expose
    private SetsListModel set_obj = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SetsListModel> getSets() {
        return sets;
    }

    public void setSets(ArrayList<SetsListModel> sets) {
        this.sets = sets;
    }
}
