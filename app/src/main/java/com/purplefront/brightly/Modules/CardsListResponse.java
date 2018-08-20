package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsListResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("images")
    @Expose
    private List<CardsListModel> images = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CardsListModel> getImages() {
        return images;
    }

    public void setImages(List<CardsListModel> images) {
        this.images = images;
    }
}
