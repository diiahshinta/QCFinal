package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Serialisasi {

    @SerializedName("Message")
    private String message;

    @SerializedName("Total")
    private int total;

    @SerializedName("Data")
    private ArrayList<DataSerialisasi> dataSerialisasi;

    @SerializedName("Result")
    private String result;

    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<DataSerialisasi> getDataSerialisasi() {
        return dataSerialisasi;
    }

    public int getTotal() {
        return total;
    }
}
