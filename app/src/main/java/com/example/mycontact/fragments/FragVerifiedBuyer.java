package com.example.mycontact.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mycontact.AddEditActivity;
import com.example.mycontact.BuyerActivity;
import com.example.mycontact.R;
import com.example.mycontact.adapter.BuyerAdapter;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragVerifiedBuyer extends Fragment implements BuyerActivity.BuyerListener {

//    variables
    public static final String HOUSE_ID_KEY = "house_id";
    private String houseId;
    private String houseName;

    //    objects
    private BuyerActivity mActivity;
    private ApiEndPoint apiEndPoint;
    private List<Buyer> listBuyer;
    private BuyerAdapter adapter;

    //    views
    private RecyclerView recContact;
    private FloatingActionButton fabAddContact;
    private Toolbar toolbarMain;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_buyer, container, false);

        initView(v);

        setActivity();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        getContactFromServer("", "Sudah Akad Kredit");
    }

    private void initView(View v) {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        toolbarMain = v.findViewById(R.id.toolbar_main);
        recContact = v.findViewById(R.id.rec_buyer);
        fabAddContact = v.findViewById(R.id.fab_add_contact);

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEditActivity.class);
                intent.putExtra(HOUSE_ID_KEY, houseId);
                startActivity(intent);
            }
        });
    }

    private void getContactFromServer(String nama, String verified) {
        Call<List<Buyer>> callContact = apiEndPoint.getAllBuyer(nama, houseId, verified);
        callContact.enqueue(new Callback<List<Buyer>>() {
            @Override
            public void onResponse(Call<List<Buyer>> call, Response<List<Buyer>> response) {
                listBuyer = response.body();

                setRecItems();
            }

            @Override
            public void onFailure(Call<List<Buyer>> call, Throwable t) {
                Toast.makeText(getContext(), "Err: " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecItems() {
        adapter = new BuyerAdapter(getActivity(), listBuyer);
        recContact.setLayoutManager(new LinearLayoutManager(getContext()));
        recContact.setAdapter(adapter);
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    @Override
    public void onBuyerSearchChange(String buyer) {
        getContactFromServer(buyer, "Sudah Akad Kredit");
    }

    public void setActivity() {
        mActivity = (BuyerActivity) getContext();
        mActivity.setBuyerListener(this);
    }
}
