package com.example.mycontact;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycontact.adapter.PagerDetailBuyer;
import com.example.mycontact.adapter.TagihanAdapter;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String BUYER_KEY = "contact";

//    ojects
    private ApiEndPoint apiEndPoint;
    private Buyer buyer;
    private PagerDetailBuyer pagerBuyerAdapter;

    //    Views
    private Toolbar toolbarDetail;
    private String buyerId;
    private MenuItem mnuEdit, mnuDelete;
    private ViewPager pagerBuyer;
    private TabLayout tabBuyer;
    private boolean isOpenFromTagihan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView() {
        isOpenFromTagihan = getIntent().getBooleanExtra(TagihanAdapter.TAGIHAN_KEY, false);

        buyerId = getIntent().getStringExtra("id");

        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        pagerBuyer = findViewById(R.id.pager_detail_buyer);
        tabBuyer = findViewById(R.id.tab_buyer_detail);
        toolbarDetail = findViewById(R.id.toolbar_detail);

        setSupportActionBar(toolbarDetail);
        setTitle("Data Konsumen");
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);
        mnuEdit = menu.findItem(R.id.action_edit);
        mnuDelete = menu.findItem(R.id.action_delete);

//        mnuEdit.setVisible(false);
//        mnuDelete.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, AddEditActivity.class);
            intent.putExtra("contact", buyer);
            intent.putExtra("action", 1);
            startActivityForResult(intent, 2);
        } else {
            deleteData();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK) {
            //buyerId = data.getStringExtra("id");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getContactFromServer(buyerId);
    }

    private void deleteData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert
                .setTitle("Hapus Data Konsumen")
                .setMessage("Anda Yakin Menghapus Konsumen, " + buyer.getNama() + "?")
                .setCancelable(false)
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteContactFromServer();
                    }
                })
                .create()
                .show();
    }

    private void deleteContactFromServer() {
        Call<Buyer> callDeleteContact = apiEndPoint.deleteBuyer(buyer.getId());
        callDeleteContact.enqueue(new Callback<Buyer>() {
            @Override
            public void onResponse(Call<Buyer> call, Response<Buyer> response) {
                Toast.makeText(DetailActivity.this, "Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Buyer> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Err: " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setupView() {
        pagerBuyerAdapter = new PagerDetailBuyer(getSupportFragmentManager(), buyer);
        pagerBuyer.setAdapter(pagerBuyerAdapter);
        tabBuyer.setupWithViewPager(pagerBuyer);

        if (isOpenFromTagihan) {
            pagerBuyer.setCurrentItem(1);
        }
    }

    private void getContactFromServer(String buyerId) {
        Call<List<Buyer>> callContact = apiEndPoint.getDetailBuyer(buyerId);
        callContact.enqueue(new Callback<List<Buyer>>() {
            @Override
            public void onResponse(Call<List<Buyer>> call, Response<List<Buyer>> response) {

                if (response.body().size() != 0) {
                    buyer = response.body().get(0);

                    setupView();

//                    setDataToView(contact);
//                    getSupportActionBar().setTitle(contact.getNama());

//                    mnuEdit.setVisible(true);
//                    mnuDelete.setVisible(true);
                }
            }

            @Override
            public void onFailure(Call<List<Buyer>> call, Throwable t) {
                Log.d("kesalahUpdate", t.getMessage());
                //Intent intent = new Intent();
                //intent.putExtra("id", buyer.getId());
                //setResult(RESULT_OK, intent);
                //finish();
            }
        });
    }
}
