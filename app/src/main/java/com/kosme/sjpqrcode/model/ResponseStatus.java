package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

public class ResponseStatus {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private Message message;

    @SerializedName("auth")
    private Auth auth;

    public Auth getAuth() {
        return auth;
    }

    public String getStatus() {
        return status;
    }

    public Message getMessage() {
        return message;
    }
}
