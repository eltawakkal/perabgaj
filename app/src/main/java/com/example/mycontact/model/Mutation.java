package com.example.mycontact.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mutation {

    @SerializedName("total_trans")
    private String totalTrans;
    @SerializedName("list_mutation")
    private List<MutationData> listMutation;

    public Mutation() {
    }

    public Mutation(String totalTrans, List<MutationData> listMutation) {
        this.totalTrans = totalTrans;
        this.listMutation = listMutation;
    }

    public String getTotalTrans() {
        return totalTrans;
    }

    public List<MutationData> getListMutation() {
        return listMutation;
    }
}
