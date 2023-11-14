package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Message {

    @SerializedName("message")
    private String message;

    @SerializedName("boxCount")
    private String box;

    @SerializedName("data")
    private ArrayList<DataBox> dataBoxArrayList;

    public String getBox() {
        return box;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<DataBox> getDataBoxArrayList() {
        return dataBoxArrayList;
    }
}
