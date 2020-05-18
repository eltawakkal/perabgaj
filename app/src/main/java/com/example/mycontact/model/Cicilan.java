package com.example.mycontact.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Cicilan {

    @SerializedName("total_cicilan")
    private String totalCicilan;
    @SerializedName("next_cicilan")
    private String nextCicilan;
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<CicilanData> cicilanData;

    public Cicilan(String totalCicilan, String nextCicilan, String status, List<CicilanData> cicilanData) {
        this.totalCicilan = totalCicilan;
        this.nextCicilan = nextCicilan;
        this.status = status;
        this.cicilanData = cicilanData;
    }

    public String getTotalCicilan() {
        return totalCicilan;
    }

    public String getNextCicilan() {
        return nextCicilan;
    }

    public String getStatus() {
        return status;
    }

    public List<CicilanData> getCicilanData() {
        return cicilanData;
    }
}
