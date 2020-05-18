package com.example.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.mycontact.adapter.PtRecAdapter;
import com.example.mycontact.model.Pt;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PtActivity extends AppCompatActivity {

    private ApiEndPoint apiEndPoint;
    private PtRecAdapter adapter;
    private List<Pt> listPt;

    private RecyclerView recPt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromServer();
    }

    void initView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        recPt = findViewById(R.id.rec_pt);
    }

    void getDataFromServer() {
        Call<List<Pt>> callPt = apiEndPoint.getAllPt("");
        callPt.enqueue(new Callback<List<Pt>>() {
            @Override
            public void onResponse(Call<List<Pt>> call, Response<List<Pt>> response) {
                listPt = response.body();

                setRecyclerItems();
            }

            @Override
            public void onFailure(Call<List<Pt>> call, Throwable t) {
                Toast.makeText(PtActivity.this,"Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setRecyclerItems() {
        adapter = new PtRecAdapter(this, listPt);
        recPt.setLayoutManager(new GridLayoutManager(this, 2));
        recPt.setAdapter(adapter);
    }
}
