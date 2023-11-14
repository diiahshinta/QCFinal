package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

public class DetailsSerialisasi {

    @SerializedName("Pallete")
    private String pallete;

    @SerializedName("Box")
    private String box;

    @SerializedName("Inner")
    private String inner;

    @SerializedName("Pcs")
    private String pcs;

    @SerializedName("Scanned")
    private String scanned;

    @SerializedName("StationName")
    private String stationName;

    public String getBox() {
        return box;
    }

    public String getInner() {
        return inner;
    }

    public String getPallete() {
        return pallete;
    }

    public String getPcs() {
        return pcs;
    }

    public String getScanned() {
        return scanned;
    }

    public String getStationName() {
        return stationName;
    }
}
