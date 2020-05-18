package com.example.mycontact.absensi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontact.R;
import com.example.mycontact.adapter.AbsenHistoryAdapter;
import com.example.mycontact.model.AbsenHistory;
import com.example.mycontact.model.GajiAndAbsenReportModel;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.example.mycontact.pref.PrimaPref;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenHistoryActivity extends AppCompatActivity {

    private ApiEndPoint apiEndPoint;
    private AbsenHistoryAdapter adapter;
    private List<AbsenHistory> listAbsen;
    private PrimaPref pref;
    private User user;
    private GajiAndAbsenReportModel gajiAndAbsenReport;

    private RecyclerView recAbsen;
    private Spinner spinMonth, spinYear;
    private TextView tvKehadiran, tvTepat, tvTelat, tvGaji, tvMakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_history);

        iniView();
    }

    private void iniView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);
        pref = new PrimaPref(this);
        listAbsen = new ArrayList<>();
        user = pref.getUser();

        boolean fromListKaryawan = getIntent().getBooleanExtra("karyawan", false);
        if (fromListKaryawan) {
            user = getIntent().getParcelableExtra("user");
        }

        recAbsen = findViewById(R.id.rec_absen_history);
        spinMonth = findViewById(R.id.spin_month_absen_history);
        spinYear = findViewById(R.id.spin_year_absen_history);
        tvKehadiran = findViewById(R.id.tv_kehadiran_absen_history);
        tvTepat = findViewById(R.id.tv_tepat_absen_history);
        tvTelat = findViewById(R.id.tv_terlambat_absen_history);
        tvGaji = findViewById(R.id.tv_gaji_absen_history);
        tvMakan = findViewById(R.id.tv_gaji_karyawan);

        DateFormat format = new SimpleDateFormat("YYYY-MM");
        Date date = Calendar.getInstance().getTime();
        String dateIn = format.format(date);

        String[] dateSparated = dateIn.split("-");

        spinMonth.setSelection(Integer.parseInt(dateSparated[1]) -1);
        spinMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getListHistory();
                getAbsenReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getListHistory();
                getAbsenReport();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getListHistory();
        getAbsenReport();
    }

    void getListHistory() {
        String year = spinYear.getSelectedItem() + "-";
        String month = year + (spinMonth.getSelectedItemPosition() + 1);

//        Toast.makeText(this, "date: " + month, Toast.LENGTH_SHORT).show();
        Call<List<AbsenHistory>> getListAbsen = apiEndPoint.getAbsenHistory(user.getId(), month);
        getListAbsen.enqueue(new Callback<List<AbsenHistory>>() {
            @Override
            public void onResponse(Call<List<AbsenHistory>> call, Response<List<AbsenHistory>> response) {
                listAbsen = response.body();
                Log.d("bisaabsenhistory", "" + listAbsen.size());
                setupRecyclerView();
            }

            @Override
            public void onFailure(Call<List<AbsenHistory>> call, Throwable t) {
                Toast.makeText(AbsenHistoryActivity.this,"Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getAbsenReport() {
        String year = spinYear.getSelectedItem() + "-";
        String month = year + (spinMonth.getSelectedItemPosition() + 1);

        Call<GajiAndAbsenReportModel> callGaji = apiEndPoint.getGajiAbsen(user.getId(), month);
        callGaji.enqueue(new Callback<GajiAndAbsenReportModel>() {
            @Override
            public void onResponse(Call<GajiAndAbsenReportModel> call, Response<GajiAndAbsenReportModel> response) {
                gajiAndAbsenReport = response.body();
                if (gajiAndAbsenReport.getGaji() != "Rp. 0") {
                    tvKehadiran.setText(gajiAndAbsenReport.getMasuk());
                    tvTepat.setText(gajiAndAbsenReport.getTepat());
                    tvTelat.setText((Integer.parseInt(gajiAndAbsenReport.getTelat()) + Integer.parseInt(gajiAndAbsenReport.getTelatT())) + "");
                    tvGaji.setText(gajiAndAbsenReport.getGajiSekarang());
                    tvMakan.setText(gajiAndAbsenReport.getMakan());
                }
            }

            @Override
            public void onFailure(Call<GajiAndAbsenReportModel> call, Throwable t) {
                Log.d("kesalahangaji", t.getMessage());
            }
        });
    }

    void setupRecyclerView() {
        adapter = new AbsenHistoryAdapter(this, listAbsen);
        recAbsen.setLayoutManager(new LinearLayoutManager(this));
        recAbsen.setAdapter(adapter);
    }
}
