package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminSettingsModel {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<AdminSettingsModuleData> settings_data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AdminSettingsModuleData> getSettings_data() {
        return settings_data;
    }

    public void setSettings_data(List<AdminSettingsModuleData> settings_data) {
        this.settings_data = settings_data;
    }
}
