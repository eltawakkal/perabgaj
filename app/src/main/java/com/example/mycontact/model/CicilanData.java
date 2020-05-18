package com.example.mycontact.model;

public class CicilanData {

    private String id;
    private String cicilan;
    private String date;
    private String type;

    public CicilanData() {
    }

    public CicilanData(String id, String cicilan, String date, String type) {
        this.id = id;
        this.cicilan = cicilan;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getCicilan() {
        return cicilan;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
