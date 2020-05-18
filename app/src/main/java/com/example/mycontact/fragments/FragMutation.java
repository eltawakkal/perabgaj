package com.example.mycontact.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontact.BuyerActivity;
import com.example.mycontact.R;
import com.example.mycontact.adapter.MutationAdapter;
import com.example.mycontact.model.Mutation;
import com.example.mycontact.model.MutationData;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.google.android.material.chip.Chip;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragMutation extends Fragment {

    private ApiEndPoint apiEndPoint;
    private MutationAdapter adapter;
    private Mutation listMutationData;

    private RecyclerView recMutasi;
    private EditText edtFromDate;
    private EditText edtToDate;
    private Chip chipToday, chipWeek, chipMonth;
    private TextView tvTitle, tvSubtitle, tvTotalTrans;

    private String houseId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_mutation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        houseId = getArguments().getString(BuyerActivity.HOUSE_ID_KEY);

        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        edtFromDate = view.findViewById(R.id.edt_from_mutation);
        edtToDate = view.findViewById(R.id.edt_to_mutation);
        chipToday = view.findViewById(R.id.chip_today);
        chipWeek = view.findViewById(R.id.chip_week);
        chipMonth = view.findViewById(R.id.chip_month);
        recMutasi = view.findViewById(R.id.rec_mutation);
        tvTitle = view.findViewById(R.id.tv_title_mutation);
        tvSubtitle = view.findViewById(R.id.tv_subtitle_mutation);
        tvTotalTrans = view.findViewById(R.id.tv_total_transaksi_mutation);

        tvSubtitle.setText("Silahkan Pilih Jangka Transaksi");
        tvTotalTrans.setText("Silahkan Pilih Jangka Transaksi");

        edtFromDate.setOnClickListener(v -> pickDate(edtFromDate));
        edtToDate.setOnClickListener(v -> pickDate(edtToDate));
        chipToday.setOnClickListener(v -> {getMutasi("", "", "1"); tvSubtitle.setText("Hari Ini");});
        chipWeek.setOnClickListener(v -> {getMutasi("", "", "7"); tvSubtitle.setText("1 Minggu Terakhir");});
        chipMonth.setOnClickListener(v -> {getMutasi("", "", "30"); tvSubtitle.setText("1 Bulan Terakhir");});
    }

    void setRecItems() {
        adapter = new MutationAdapter(listMutationData.getListMutation(), getContext());
        recMutasi.setLayoutManager(new LinearLayoutManager(getContext()));
        recMutasi.setAdapter(adapter);
    }

    void pickDate(EditText edt) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edt.setText(day + "-"  + (month+1) + "-" + year);

                if (!edtFromDate.getText().toString().equals("") && !edtToDate.getText().toString().equals("")) {
                    String fromDate = edtFromDate.getText().toString();
                    String toDate = edtToDate.getText().toString();

                    String[] splittedStringFrom = fromDate.split("-");
                    String[] splittedStringTo = toDate.split("-");
                    String fixFromDate = splittedStringFrom[2] + "-" + splittedStringFrom[1] + "-" + splittedStringFrom[0]; //dibalik aja wkkw
                    String fixToDate = splittedStringTo[2] + "-" + splittedStringTo[1] + "-" + splittedStringTo[0]; //dibalik aja wkkw

                    getMutasi(fixFromDate, fixToDate, "0");

                    tvSubtitle.setText("(" + fromDate + ") sampai (" + toDate + ")");
                }

            }
        }, year, month, day);
        datePickerDialog.show();
    }

    void getMutasi(String from, String to, String range) {
        Call<Mutation> callMutasi = apiEndPoint.mutasiTransaksi(houseId, from, to, range);
        callMutasi.enqueue(new Callback<Mutation>() {
            @Override
            public void onResponse(Call<Mutation> call, Response<Mutation> response) {
                listMutationData = response.body();

                tvTotalTrans.setText(listMutationData.getTotalTrans());
                setRecItems();
            }

            @Override
            public void onFailure(Call<Mutation> call, Throwable t) {
                Log.d("gagalMutasi", t.getMessage());
            }
        });
    }
}
