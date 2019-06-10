package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminSettingsModuleData {

    @SerializedName("labelName")
    @Expose
    private String labelName;

    @SerializedName("webLink")
    @Expose
    private String webLink;

    @SerializedName("webIcon")
    @Expose
    private String webIcon;


    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getWebIcon() {
        return webIcon;
    }

    public void setWebIcon(String webIcon) {
        this.webIcon = webIcon;
    }
}
