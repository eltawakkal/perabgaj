package com.example.mycontact.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontact.DetailActivity;
import com.example.mycontact.R;
import com.example.mycontact.adapter.CicilanAdapter;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.model.Cicilan;
import com.example.mycontact.model.Transaction;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragDetailBuyerPayment extends Fragment implements CicilanAdapter.RecyclerOnItemClickHandler {

    private ApiEndPoint apiEndPoint;
    private Buyer buyer;
    private Cicilan listBuyer;
    private CicilanAdapter adapter;

    private RecyclerView recCicilan;
    private TextView tvTotalCicilan, tvTitleAddCicilan, tvMessageAddCicilan;
    private RelativeLayout rlAddPayment;
    private ImageView imgStatusTagihan;
    private FloatingActionButton fanAddCicilan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_buyer_detail_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        recCicilan = v.findViewById(R.id.rec_cicilan);
        tvTotalCicilan = v.findViewById(R.id.tv_total_cicilan);
        tvTitleAddCicilan = v.findViewById(R.id.tv_title_add_cicilan);
        tvMessageAddCicilan = v.findViewById(R.id.tv_message_add_cicilan);
//        fanAddCicilan = v.findViewById(R.id.fab_add_cicilan);
        rlAddPayment = v.findViewById(R.id.rl_add_payment);
        imgStatusTagihan = v.findViewById(R.id.img_status_cicilan_cicilan);


        buyer = getArguments().getParcelable(DetailActivity.BUYER_KEY);
        getCicilanFromServer();
    }

    void getCicilanFromServer() {
        Call<Cicilan> callCicilan = apiEndPoint.getCicilan(buyer.getId(), buyer.getHouseId(), buyer.getUnit());
        callCicilan.enqueue(new Callback<Cicilan>() {
            @Override
            public void onResponse(Call<Cicilan> call, Response<Cicilan> response) {
                listBuyer = null;
                listBuyer = response.body();

                setRecItems();
                tvTotalCicilan.setText(listBuyer.getTotalCicilan());

                if (listBuyer.getCicilanData().size() != 0) {
                    if (!listBuyer.getCicilanData().get(listBuyer.getCicilanData().size()-1).getType().equals("Selesai")) {
                        tvTitleAddCicilan.setText("Selanjutnya, Cicilan Ke " + (listBuyer.getCicilanData().size()));
                        tvMessageAddCicilan.setText("Akan Jatuh Tempo Pada " + listBuyer.getNextCicilan());
                    } else {
                        tvTitleAddCicilan.setText("Lunas");
                        tvMessageAddCicilan.setText("Semua cicilan telah diselesaikan");
                    }
                    if (listBuyer.getStatus().equals("telat")) {
                        imgStatusTagihan.setImageResource(R.drawable.ic_warning);
                    } else {
                        imgStatusTagihan.setImageResource(R.drawable.ic_done);
                    }
                } else {
                    tvTitleAddCicilan.setText("Pembayaran belum ada");
                     tvMessageAddCicilan.setText("Hubungi Admin Untuk Info Lebih Lanjut");
                }
            }

            @Override
            public void onFailure(Call<Cicilan> call, Throwable t) {

            }
        });
    }

    void setRecItems() {
        adapter = new CicilanAdapter(listBuyer, getContext(), this);
        recCicilan.setLayoutManager(new LinearLayoutManager(getContext()));
        recCicilan.setAdapter(adapter);
    }

    void addCicilanView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_add_cicilan, null, false);

        EditText edtCicilan = view.findViewById(R.id.edt_cicilan_cicilan);
        EditText edtTransDate = view.findViewById(R.id.edt_trans_date_cicilan);
        CheckBox chkSelesaiCicilan = view.findViewById(R.id.chk_selesai_cicilan);
        MaterialButton btnAddPayment = view.findViewById(R.id.btn_add_payment_to_server);

        edtTransDate.setOnClickListener(v -> pickDate(edtTransDate));

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//        alert
//                .setCancelable(false)
//                .setTitle("Tambah Transaksi Baru")
//                .setNegativeButton("Batal", null)
//                .setPositiveButton("Tambah", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//
//                    }
//                });

        alert.setView(view);
        Dialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        btnAddPayment.setOnClickListener(v -> {
            String cicilan = edtCicilan.getText().toString();
            String transDate = edtTransDate.getText().toString();
            boolean selesai = chkSelesaiCicilan.isChecked();

            addDataToServer(cicilan, transDate, selesai);

            dialog.dismiss();
        });
    }

    void deletePayment(int position) {
        Call<Transaction> deletePay = apiEndPoint.deletePayment(listBuyer.getCicilanData().get(position).getId());
        deletePay.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                getCicilanFromServer();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {

            }
        });
    }

    void addDataToServer(String cicilan, String transDate, boolean lunas) {
        String type = "";

        if (listBuyer.getCicilanData().size() == 0) {
            type = "Booking Fee";
        } else {
            if (lunas) {
                type = "Selesai";
            } else {
                type = "Cicilan Ke " + (listBuyer.getCicilanData().size());
            }
        }

        Call<Transaction> callCicilan = apiEndPoint.addCicilan(buyer.getId(), buyer.getHouseId(), buyer.getUnit(), cicilan, transDate, type);
        callCicilan.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                getCicilanFromServer();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Log.d("gagalAddCicilan", t.getMessage());
            }
        });

    }

    void pickDate(EditText edt) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edt.setText(year + "-"  + (month+1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onItemClicked(int position) {
        deleteDataView(position);
    }

    void deleteDataView(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert
                            .setTitle("Hapus Pembayaran")
                            .setMessage("Yakin Hapus Pembayaran " + listBuyer.getCicilanData().get(position).getType())
                            .setNegativeButton("Batal", null)
                            .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePayment(position);
                                }
                            })
                    .show();
    }

}
