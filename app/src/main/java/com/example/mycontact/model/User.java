package com.example.mycontact.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    private String id;
    @SerializedName("created_at")
    private String createdAt;
    private String name;
    private String email;
    private String password;
    private String device;
    @SerializedName("photo_url")
    private String photo;
    private String gaji;
    private String makan;

    public User() {
    }

    public User(String id, String createdAt, String name, String email, String password, String device, String photo, String gaji, String makan) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.device = device;
        this.photo = photo;
        this.gaji = gaji;
        this.makan = makan;
    }

    public User(String id, String createdAt, String name, String email, String password, String device, String photo) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.email = email;
        this.password = password;
        this.device = device;
        this.photo = photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDevice() {
        return device;
    }

    public String getPhoto() {
        return photo;
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    protected User(Parcel in) {
        id = in.readString();
        createdAt = in.readString();
        name = in.readString();
        email = in.readString();
        password = in.readString();
        device = in.readString();
        photo = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdAt);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(device);
        dest.writeString(photo);
    }

    public String getGaji() {
        return gaji;
    }

    public void setGaji(String gaji) {
        this.gaji = gaji;
    }

    public String getMakan() {
        return makan;
    }

    public void setMakan(String makan) {
        this.makan = makan;
    }
}
