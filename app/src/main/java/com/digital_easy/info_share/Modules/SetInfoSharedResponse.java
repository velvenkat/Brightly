package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SetInfoSharedResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("shared_data")
    @Expose
    private ArrayList<SharedDataModel> shared_data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SharedDataModel> getShared_data() {
        return shared_data;
    }

    public void setShared_data(ArrayList<SharedDataModel> shared_data) {
        this.shared_data = shared_data;
    }
}
