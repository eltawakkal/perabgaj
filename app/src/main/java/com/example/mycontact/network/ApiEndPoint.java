package com.example.mycontact.network;

import com.example.mycontact.model.AbsenHistory;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.model.Cicilan;
import com.example.mycontact.model.GajiAndAbsenReportModel;
import com.example.mycontact.model.House;
import com.example.mycontact.model.Mutation;
import com.example.mycontact.model.MutationData;
import com.example.mycontact.model.Pt;
import com.example.mycontact.model.Tagihan;
import com.example.mycontact.model.Transaction;
import com.example.mycontact.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiEndPoint {

    @FormUrlEncoded
    @POST("get-all-pt.php")
    Call<List<Pt>> getAllPt(@Field("nama") String name);

    @FormUrlEncoded
    @POST("get-all-house.php")
    Call<List<House>> getAllHouse(
            @Field("nama") String name,
            @Field("pt_id") String ptId);

    @FormUrlEncoded
    @POST("get-buyer.php")
    Call<List<Buyer>> getAllBuyer(
            @Field("nama") String nama,
            @Field("home_id") String homeId,
            @Field("verified") String verified);

    @FormUrlEncoded
    @POST("get-detail-buyer.php")
    Call<List<Buyer>> getDetailBuyer(@Field("id") String id);

    @FormUrlEncoded
    @POST("delete-buyer.php")
    Call<Buyer> deleteBuyer(@Field("id") String id);

    @FormUrlEncoded
    @POST("add-buyer.php")
    Call<Transaction> addBuyer(
            @Field("harga_jual") String harga_jual,
            @Field("cp") String cp,
            @Field("unit") String unit,
            @Field("house_id") String houseId,
            @Field("nama") String nama,
            @Field("no_telp")String noTelp,
            @Field("no_pasangan")String no_pasangan,
            @Field("no_darurat")String no_darurat,
            @Field("alamat")String alamat,
            @Field("pekerjaan")String pekerjaan,
            @Field("status_berkas")String status_berkas,
            @Field("tgl_booking")String tgl_booking,
            @Field("bank_pel")String bank_pel,
            @Field("ket")String ket,
            @Field("a")String a,
            @Field("b")String b,
            @Field("c")String c,
            @Field("d")String d,
            @Field("e")String e,
            @Field("f")String f,
            @Field("g")String g,
            @Field("verified")String verified);

    @FormUrlEncoded
    @POST("update-buyer.php")
    Call<Transaction> updateBuyer(
            @Field("harga_jual") String harga_jual,
            @Field("cp") String cp,
            @Field("unit") String unit,
            @Field("nama") String nama,
            @Field("no_telp")String noTelp,
            @Field("no_pasangan")String no_pasangan,
            @Field("no_darurat")String no_darurat,
            @Field("alamat")String alamat,
            @Field("pekerjaan")String pekerjaan,
            @Field("status_berkas")String status_berkas,
            @Field("tgl_booking")String tgl_booking,
            @Field("bank_pel")String bank_pel,
            @Field("ket")String ket,
            @Field("a")String a,
            @Field("b")String b,
            @Field("c")String c,
            @Field("d")String d,
            @Field("e")String e,
            @Field("f")String f,
            @Field("g")String g,
            @Field("verified")String verified);

    @FormUrlEncoded
    @POST("get-cicilan.php")
    Call<Cicilan> getCicilan(
            @Field("buyer_id") String buyerId,
            @Field("house_id") String houseId,
            @Field("unit") String unit
    );

    @FormUrlEncoded
    @POST("add-cicilan.php")
    Call<Transaction> addCicilan(
            @Field("buyer_id") String buyerId,
            @Field("house_id") String houseId,
            @Field("unit") String unit,
            @Field("cicilan") String cicilan,
            @Field("trans_date") String transDate,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("mutasi-cicilan.php")
    Call<Mutation> mutasiTransaksi(
            @Field("house_id") String houseId,
            @Field("start_date") String startDate,
            @Field("end_date") String endDate,
            @Field("range") String range
    );

    @FormUrlEncoded
    @POST("get-late-transaction.php")
    Call<List<Tagihan>> getTagihan(@Field("house_id") String houseId);

    @FormUrlEncoded
    @POST("delete-payment.php")
    Call<Transaction> deletePayment(@Field("pay_id") String payId);

    @FormUrlEncoded
    @POST("check-user.php")
    Call<User> checkUser(@Field("id") String userId);

    @FormUrlEncoded
    @POST("add-user.php")
    Call<User> addUser(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("device") String userDevice,
            @Field("photo") String photo
    );

    @FormUrlEncoded
    @POST("check-absen.php")
    Call<List<User>> checkAbsen(
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("add-absen.php")
    Call<User> addAbsen(
            @Field("user_id") String userId,
            @Field("type") int type
    );

    @FormUrlEncoded
    @POST("get-absen-history.php")
    Call<List<AbsenHistory>> getAbsenHistory(
            @Field("user_id") String userId,
            @Field("date_in") String dateIn
    );

    @FormUrlEncoded
    @POST("absen-report.php")
    Call<GajiAndAbsenReportModel> getGajiAbsen(
            @Field("user_id") String userId,
            @Field("date_in") String dateIn
    );

    @GET("get-karyawan.php")
    Call<List<User>> getKaryawan();

    @FormUrlEncoded
    @POST("update-gaji.php")
    Call<Transaction> updateGaji(
            @Field("user_id") String userId,
            @Field("gaji") String gaji,
            @Field("makan") String makan
    );

}
