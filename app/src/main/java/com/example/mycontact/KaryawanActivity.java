package com.example.mycontact;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontact.absensi.AbsenHistoryActivity;
import com.example.mycontact.adapter.KaryawanAdapter;
import com.example.mycontact.model.AbsenHistory;
import com.example.mycontact.model.Transaction;
import com.example.mycontact.model.User;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KaryawanActivity extends AppCompatActivity implements KaryawanAdapter.OnClick{

    private ApiEndPoint apiEndPoint;
    private List<User> listUser;
    private RecyclerView recKaryawan;
    private KaryawanAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karyawan);

        initView();
    }

    @Override
    public void onItemClick(int position) {
        showGajiKaryawan(position);
    }

    private void initView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        progressDialog = new ProgressDialog(this);
        listUser = new ArrayList<>();
        adapter = new KaryawanAdapter(listUser, this, this);

        recKaryawan = findViewById(R.id.rec_karyawan);

        recKaryawan.setLayoutManager(new LinearLayoutManager(this));
        recKaryawan.setAdapter(adapter);

        progressDialog.setTitle("Tunggu");
        progressDialog.setMessage("Memperbaharui data...");

        getKaryawanFromServer();
    }

    private void getKaryawanFromServer() {
        progressDialog.show();
        Call<List<User>> callKaryawan = apiEndPoint.getKaryawan();
        callKaryawan.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> listTamp = response.body();
                listUser.clear();
                listUser.addAll(listTamp);

                if (listUser != null) {
                    adapter.notifyDataSetChanged();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(KaryawanActivity.this, "Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void updateGajiKaryawan(int position, String gaji, String makan) {
        User user = listUser.get(position);
        Call<Transaction> updateGaji = apiEndPoint.updateGaji(user.getId(), gaji, makan);
        updateGaji.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                getKaryawanFromServer();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.d("kesalahupdategaji", t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    void showGajiKaryawan(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.view_gaji_karyawan, null, false);

        ImageView imgKaryawan = v.findViewById(R.id.img_view_karyawan);
        TextView tvName = v.findViewById(R.id.tv_nama_view_karyawan);
        TextView tvEmail = v.findViewById(R.id.tv_email_view_karyawan);
        EditText edtGaji = v.findViewById(R.id.edt_gaji_view_karyawan);
        EditText edtMakan = v.findViewById(R.id.edt_makan_view_karyawan);

        try {
            Picasso.get().load(listUser.get(position).getPhoto()).into(imgKaryawan);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.user).into(imgKaryawan);
        }

        tvName.setText(listUser.get(position).getName());
        tvEmail.setText(listUser.get(position).getEmail());
        edtGaji.setText(listUser.get(position).getGaji());
        edtMakan.setText(listUser.get(position).getMakan());

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(v);
        alert.setCancelable(false);
        alert.setNegativeButton("batal", null);
        alert.setNeutralButton("Rincian Kehadiran", ((dialog, which) -> {
            User user = new User();
            user.setId(listUser.get(position).getId());
            Intent intent = new Intent(this, AbsenHistoryActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("karyawan", true);
            startActivity(intent);
        }));
        alert.setPositiveButton("simpan", (dialog, which) -> {

            String gaji = edtGaji.getText().toString();
            String makan = edtMakan.getText().toString();

            updateGajiKaryawan(position, gaji, makan);

        });
        alert.show();
    }
}
