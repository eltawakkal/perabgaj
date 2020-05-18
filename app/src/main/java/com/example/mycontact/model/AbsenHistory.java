package com.example.mycontact.model;

import com.google.gson.annotations.SerializedName;

public class AbsenHistory {

    private String id;
    private String dateIn;
    private String timeIn;
    private String name;
    private String status;
    private String type;

    public AbsenHistory() {
    }

    public AbsenHistory(String id, String dateIn, String timeIn, String name, String status, String type) {
        this.id = id;
        this.dateIn = dateIn;
        this.timeIn = timeIn;
        this.name = name;
        this.status = status;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
