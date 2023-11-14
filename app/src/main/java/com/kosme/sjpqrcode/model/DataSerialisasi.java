package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataSerialisasi {

    @SerializedName("Barcode")
    private String barcode;

    @SerializedName("Count")
    private int count;

    @SerializedName("Details")
    private ArrayList<DetailsSerialisasi> detailsSerialisasi;

    public ArrayList<DetailsSerialisasi> getDetailsSerialisasi() {
        return detailsSerialisasi;
    }

    public int getCount() {
        return count;
    }

    public String getBarcode() {
        return barcode;
    }
}
