package com.example.mycontact.model;

import com.google.gson.annotations.SerializedName;

public class MutationData {

    private String nama;
    private String cicilan;
    private String type;
    @SerializedName("trans_date")
    private String transDate;

    public MutationData() {
    }

    public MutationData(String nama, String cicilan, String type, String transDate) {
        this.nama = nama;
        this.cicilan = cicilan;
        this.type = type;
        this.transDate = transDate;
    }

    public String getNama() {
        return nama;
    }

    public String getCicilan() {
        return cicilan;
    }

    public String getType() {
        return type;
    }

    public String getTransDate() {
        return transDate;
    }

}
