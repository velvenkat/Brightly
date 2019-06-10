package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultipleImageModel {
    @SerializedName("id")
    @Expose
    private String img_id = "";

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @SerializedName("image_name")
    @Expose
    private String img_url = "";

    public String getImg_encode_string() {
        return img_encode_string;
    }

    public void setImg_encode_string(String img_encode_string) {
        this.img_encode_string = img_encode_string;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    private String img_encode_string = "";

    private String img_name = "";

}
