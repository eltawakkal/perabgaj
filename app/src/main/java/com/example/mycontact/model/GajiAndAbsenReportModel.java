package com.example.mycontact.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GajiAndAbsenReportModel implements Parcelable {

    @SerializedName("user_id")
    private String userId;
    private String masuk;
    @SerializedName("gaji_now")
    private String gajiSekarang;
    private String gaji;
    private String makan;
    private String absen;
    private String tepat;
    private String telatT;
    private String telat;

    public GajiAndAbsenReportModel() {
    }

    public GajiAndAbsenReportModel(String userId, String masuk, String gajiSekarang, String gaji, String makan, String absen, String tepat, String telatT, String telat) {
        this.userId = userId;
        this.masuk = masuk;
        this.gajiSekarang = gajiSekarang;
        this.gaji = gaji;
        this.makan = makan;
        this.absen = absen;
        this.tepat = tepat;
        this.telatT = telatT;
        this.telat = telat;
    }

    protected GajiAndAbsenReportModel(Parcel in) {
        userId = in.readString();
        masuk = in.readString();
        gajiSekarang = in.readString();
        gaji = in.readString();
        makan = in.readString();
        absen = in.readString();
        tepat = in.readString();
        telatT = in.readString();
        telat = in.readString();
    }

    public static final Creator<GajiAndAbsenReportModel> CREATOR = new Creator<GajiAndAbsenReportModel>() {
        @Override
        public GajiAndAbsenReportModel createFromParcel(Parcel in) {
            return new GajiAndAbsenReportModel(in);
        }

        @Override
        public GajiAndAbsenReportModel[] newArray(int size) {
            return new GajiAndAbsenReportModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMasuk() {
        return masuk;
    }

    public void setMasuk(String masuk) {
        this.masuk = masuk;
    }

    public String getGajiSekarang() {
        return gajiSekarang;
    }

    public void setGajiSekarang(String gajiSekarang) {
        this.gajiSekarang = gajiSekarang;
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

    public String getAbsen() {
        return absen;
    }

    public void setAbsen(String absen) {
        this.absen = absen;
    }

    public String getTepat() {
        return tepat;
    }

    public void setTepat(String tepat) {
        this.tepat = tepat;
    }

    public String getTelatT() {
        return telatT;
    }

    public void setTelatT(String telatT) {
        this.telatT = telatT;
    }

    public String getTelat() {
        return telat;
    }

    public void setTelat(String telat) {
        this.telat = telat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(masuk);
        dest.writeString(gajiSekarang);
        dest.writeString(gaji);
        dest.writeString(makan);
        dest.writeString(absen);
        dest.writeString(tepat);
        dest.writeString(telatT);
        dest.writeString(telat);
    }
}
