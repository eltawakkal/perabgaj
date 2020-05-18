package com.example.mycontact.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontact.BuyerActivity;
import com.example.mycontact.R;
import com.example.mycontact.adapter.TagihanAdapter;
import com.example.mycontact.model.Tagihan;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragTagihan extends Fragment {

    private ApiEndPoint apiEndPoint;
    private TagihanAdapter adapter;
    private List<Tagihan> listTagijan;

    private RecyclerView recTagihan;

    private String houseId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_tagihan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        houseId = getArguments().getString(BuyerActivity.HOUSE_ID_KEY);

        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        recTagihan = view.findViewById(R.id.rec_tagihan);

        getTagihanFromServer();
    }

    void getTagihanFromServer() {
        Call<List<Tagihan>> callTagihan = apiEndPoint.getTagihan(houseId);
        callTagihan.enqueue(new Callback<List<Tagihan>>() {
            @Override
            public void onResponse(Call<List<Tagihan>> call, Response<List<Tagihan>> response) {
                listTagijan = response.body();
                setRecItem();
            }

            @Override
            public void onFailure(Call<List<Tagihan>> call, Throwable t) {
                Log.d("gagalTagihan", t.getMessage().toString());
            }
        });
    }

    //post man

    void setRecItem() {
        adapter = new TagihanAdapter(listTagijan, getContext());
        recTagihan.setLayoutManager(new LinearLayoutManager(getContext()));
        recTagihan.setAdapter(adapter);
    }
}
