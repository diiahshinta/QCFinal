package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

public class DataBox {

    @SerializedName("Serialisasi")
    private String serialisasi;

    public String getSerialisasi() {
        return serialisasi;
    }
}
