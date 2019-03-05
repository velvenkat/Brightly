package com.purplefront.brightly.Modules;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatListResponse {
    @SerializedName("message")
    @Expose
    private String message;

   
    @SerializedName("contacts")
    @Expose
    private List<ChatContacts> contacts;

}
