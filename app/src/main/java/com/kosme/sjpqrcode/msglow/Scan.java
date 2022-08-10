package com.kosme.sjpqrcode.msglow;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Scan {

    @SerializedName("Barcode")
    private String barcode;

    @SerializedName("sku")
    private String sku;

    @SerializedName("nie")
    private String nie;

    @SerializedName("Parent_ID")
    private String parent;

    @SerializedName("product")
    private String product;

    @SerializedName("md")
    private String md;

    @SerializedName("ed")
    private String ed;

    @SerializedName("Batch_No")
    private String batch;

    @SerializedName("sjp")
    private String sjp;

    @SerializedName("pcs")
    private ArrayList<String> pcs;

    public String getSku() {
        return sku;
    }

    public String getNie() {
        return nie;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getBatch() {
        return batch;
    }

    public String getMd() {
        return md;
    }

    public String getEd() {
        return ed;
    }

    public String getParent() {
        return parent;
    }

    public ArrayList<String> getPcs() {
        return pcs;
    }

    public String getProduct() {
        return product;
    }

    public String getSjp() {
        return sjp;
    }
}
