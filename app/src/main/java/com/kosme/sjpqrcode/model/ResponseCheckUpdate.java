package com.kosme.sjpqrcode.model;

import com.google.gson.annotations.SerializedName;

public class ResponseCheckUpdate {

    @SerializedName("id_station")
    private String id;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("serialisasi")
    private String serialisasi;

    @SerializedName("product")
    private String product;

    @SerializedName("gcp_carton")
    private String karton = null;

    @SerializedName("gcp_wrap")
    private String wrap = null;

    @SerializedName("gcp_pieces")
    private String pieces = null;

    @SerializedName("local_carton")
    private int localCarton;

    @SerializedName("local_wrap")
    private int localWrap;

    @SerializedName("local_pieces")
    private int localPieces;

    @SerializedName("sjp")
    private String sjp = null;

    @SerializedName("status_monitoring")
    private String status;

    @SerializedName("description")
    private String desc;

    @SerializedName("created_at")
    private String created = null;

    public String getSerialisasi() {
        return serialisasi;
    }

    public int getLocalCarton() {
        return localCarton;
    }

    public int getLocalPieces() {
        return localPieces;
    }

    public int getLocalWrap() {
        return localWrap;
    }

    public String getId() {
        return id;
    }

    public String getPieces() {
        return pieces;
    }

    public String getStatus() {
        return status;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDesc() {
        return desc;
    }

    public String getSjp() {
        return sjp;
    }

    public String getProduct() {
        return product;
    }

    public String getCreated() {
        return created;
    }

    public String getKarton() {
        return karton;
    }

    public String getWrap() {
        return wrap;
    }
}
