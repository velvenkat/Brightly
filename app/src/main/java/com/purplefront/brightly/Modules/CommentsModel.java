package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentsModel {
    @SerializedName("comment")
    @Expose
    public String comment;

    @SerializedName("comment_mode")
    @Expose
    public String comment_mode;

    @SerializedName("created_by")
    @Expose
    public String created_by;

    @SerializedName("created")
    @Expose
    public String created_time;

    @SerializedName("name")
    @Expose
    public String created_name;

    @SerializedName("mobile")
    @Expose
    public String mobile_no;

    @SerializedName("id")
    @Expose
    public String card_id;
    
    @SerializedName("registered_status")
    @Expose
    public String registered_status;

}
