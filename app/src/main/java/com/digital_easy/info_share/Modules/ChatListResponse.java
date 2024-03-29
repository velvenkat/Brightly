package com.digital_easy.info_share.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatListResponse {
    @SerializedName("message")
    @Expose
    private String message;


    @SerializedName("data")
    @Expose
    private List<CommentsModel> contacts;

}
