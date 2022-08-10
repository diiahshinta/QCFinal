package com.kosme.sjpqrcode.msglow;

import com.google.gson.annotations.SerializedName;

public class ModelData {


    @SerializedName("scan")
    private Scan scan;

    public Scan getScan() {
        return scan;
    }
}
