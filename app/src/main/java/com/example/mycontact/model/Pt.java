package com.example.mycontact.model;

public class Pt {

    private String id;
    private String nama;
    private String alamat;
    private String photo_url;

    public Pt(String id, String nama, String alamat, String photo_url) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.photo_url = photo_url;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getPhoto_url() {
        return photo_url;
    }
}
