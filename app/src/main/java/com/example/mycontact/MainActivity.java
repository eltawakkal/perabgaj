package com.example.mycontact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycontact.absensi.AbsenActivity;
import com.example.mycontact.absensi.AbsenHistoryActivity;
import com.example.mycontact.adapter.PagerMainActivityAdapter;
import com.example.mycontact.login.LoginActivity;
import com.example.mycontact.model.GajiAndAbsenReportModel;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.example.mycontact.pref.PrimaPref;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiEndPoint apiEndPoint;
    private PrimaPref pref;
    private User user;
    private GajiAndAbsenReportModel gajiAndAbsenReport;
    private ImageView imgProfile;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvGaji;

    private PagerMainActivityAdapter adapter;
    private ViewPager pagerMain;
    private TabLayout tabMain;
    private int[] arrImages;
    private int pagerPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrImages = new int[]{
                R.drawable.viewpager1,
                R.drawable.noimage,
                R.drawable.noimage,
                R.drawable.noimage
        };

        adapter = new PagerMainActivityAdapter(arrImages, this);

        pagerMain = findViewById(R.id.pager_main);
        tabMain = findViewById(R.id.tab_main);

        pagerMain.setAdapter(adapter);
        tabMain.setupWithViewPager(pagerMain);

        initView();
    }

    private void initView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
        pref = new PrimaPref(this);
        user = pref.getUser();

        imgProfile = findViewById(R.id.img_profile_main);
        tvName = findViewById(R.id.tv_name_main);
        tvEmail = findViewById(R.id.tv_email_main);
        tvGaji = findViewById(R.id.tv_total_gaji);

        try {
            Picasso.get().load(user.getPhoto()).placeholder(R.drawable.user).into(imgProfile);
        } catch (Exception e){
            Picasso.get().load(R.drawable.user).into(imgProfile);
        }

        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new PagerTimer(), 2000, 4000);
    }

    void setPagerAds() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        getGajiAbsenFromServer(user.getId());
    }

    public void gotoAbsen(View v) {
        startActivity(new Intent(this, AbsenActivity.class));
    }

    public void gotoHistory(View v) {
        startActivity(new Intent(this, AbsenHistoryActivity.class));
    }

    public void gotoRumah(View v) {
        startActivity(new Intent(this, PtActivity.class));
    }

    public void gotoKaryawan(View v) {
        startActivity(new Intent(this, KaryawanActivity.class));
    }

    public void logout(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setTitle("Logout")
                .setMessage("Logout Sekarang")
                .setCancelable(false)
                .setNegativeButton("Batal", null)
                .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pref.removeUser();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .show();
    }

    private void getGajiAbsenFromServer(String userId) {
        DateFormat format = new SimpleDateFormat("YYYY-MM");
        Date date = Calendar.getInstance().getTime();
        String dateIn = format.format(date);

        Call<GajiAndAbsenReportModel> callGaji = apiEndPoint.getGajiAbsen(userId, dateIn);
        callGaji.enqueue(new Callback<GajiAndAbsenReportModel>() {
            @Override
            public void onResponse(Call<GajiAndAbsenReportModel> call, Response<GajiAndAbsenReportModel> response) {
                gajiAndAbsenReport = response.body();
                if (gajiAndAbsenReport.getGaji() != "Rp. 0") {
                    tvGaji.setText(gajiAndAbsenReport.getGajiSekarang());
                }
            }

            @Override
            public void onFailure(Call<GajiAndAbsenReportModel> call, Throwable t) {
                Log.d("kesalahangaji", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah anda ingin meninggalkan Prima Group ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    class PagerTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pagerMain.getCurrentItem() == 0) {
                        pagerMain.setCurrentItem(1);
                        Log.d("pager", pagerMain.getCurrentItem() + "");
                    } else if (pagerMain.getCurrentItem() == 1){
                        pagerMain.setCurrentItem(2);
                        Log.d("pager", pagerMain.getCurrentItem() + "");
                    } else if (pagerMain.getCurrentItem() == 2){
                        pagerMain.setCurrentItem(3);
                        Log.d("pager", pagerMain.getCurrentItem() + "");
                    } else {
                        Log.d("pager", "pass");
                        pagerMain.setCurrentItem(0);
                    }

                   // Log.d("pagerPosition", "Size: " + arrImages.length + " State: " + pagerPosition + "");
                }
            });
        }
    }
}
