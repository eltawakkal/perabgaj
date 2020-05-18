package com.example.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.mycontact.adapter.HouseRecAdapter;
import com.example.mycontact.model.House;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseActivity extends AppCompatActivity {

//    variables
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    private String ptId;
    private String ptName;

//    objects
    private ApiEndPoint apiEndPoint;
    private HouseRecAdapter adapter;
    private List<House> listHouse;

//    views
    private RecyclerView recHouse;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getDataFromServer();
    }

    private void getDataFromServer() {
        Call<List<House>> callHouse = apiEndPoint.getAllHouse("", ptId);
        callHouse.enqueue(new Callback<List<House>>() {
            @Override
            public void onResponse(Call<List<House>> call, Response<List<House>> response) {
                listHouse = response.body();

                setRecItems();
            }

            @Override
            public void onFailure(Call<List<House>> call, Throwable t) {
                Toast.makeText(HouseActivity.this, "Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecItems() {
        adapter = new HouseRecAdapter(this, listHouse);
        recHouse.setLayoutManager(new LinearLayoutManager(this));
        recHouse.setAdapter(adapter);
    }

    private void initView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        ptId = getIntent().getStringExtra(ID_KEY);
        ptName = getIntent().getStringExtra(NAME_KEY);

        recHouse = findViewById(R.id.rec_house);
        toolbar = findViewById(R.id.toolbar_house);

        setSupportActionBar(toolbar);
        setTitle(ptName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
