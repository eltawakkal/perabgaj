package com.example.mycontact.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mycontact.DetailActivity;
import com.example.mycontact.R;
import com.example.mycontact.model.Buyer;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragDetailBuyerData extends Fragment {

//    ojects
    private ApiEndPoint apiEndPoint;
    private Buyer buyer;

    //    Views
    private Toolbar toolbarDetail;
    private TextView tvharga_jual;
    private TextView tvUnit;
    private TextView tvCp;
    private TextView tvNama;
    private TextView tvNoTelp;
    private TextView tvno_pasangan;
    private TextView tvno_darurat;
    private TextView tvAlamat;
    private TextView tvPekerjaan;
    private TextView tvstatus_berkas;
    private TextView tvtgl_booking;
    private TextView tvbank_pel;
    private TextView tvKet;
    private TextView tvtotal_pembayaran;
    /*private TextView a;
    private TextView b;
    private TextView c;
    private TextView d;
    private TextView e;
    private TextView f;
    private TextView g;*/

    //bisa juga
    private TextView a, b, c, d, e, f, g;
//    private TextView bayarfee, bayar1, bayar2, bayar3, bayar4;

    private String buyerId;
    private MenuItem mnuEdit, mnuDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        buyer = getArguments().getParcelable(DetailActivity.BUYER_KEY);
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        toolbarDetail = v.findViewById(R.id.toolbar_detail);
        tvharga_jual = v.findViewById(R.id.tvharga_jual);
        tvUnit = v.findViewById(R.id.tv_detail_unit);
        tvCp = v.findViewById(R.id.tvcp);
        tvNama = v.findViewById(R.id.tv_detail_nama);
        tvNoTelp = v.findViewById(R.id.tv_detail_notelp);
        tvno_pasangan = v.findViewById(R.id.tv_detail_no_pasangan);
        tvno_darurat = v.findViewById(R.id.tv_detail_no_darurat);
        tvAlamat = v.findViewById(R.id.tv_detail_alamat);
        tvPekerjaan = v.findViewById(R.id.tv_detail_pekerjaan);
        tvstatus_berkas = v.findViewById(R.id.tv_detail_sts_berkas);
        tvtgl_booking = v.findViewById(R.id.tv_detail_tgl_booking);
        tvbank_pel = v.findViewById(R.id.tv_detail_bank_pel);
        tvKet = v.findViewById(R.id.tv_detail_keterangan);
        a = v.findViewById(R.id.tv_detail_a);
        b = v.findViewById(R.id.tv_detail_b);
        c = v.findViewById(R.id.tv_detail_c);
        d = v.findViewById(R.id.tv_detail_d);
        e = v.findViewById(R.id.tv_detail_e);
        f = v.findViewById(R.id.tv_detail_f);
        g = v.findViewById(R.id.tv_detail_g);

        setDataToView(buyer);
//        getContactFromServer(buyerId);
    }

    private void getContactFromServer(String id) {
        Call<List<Buyer>> callContact = apiEndPoint.getDetailBuyer(id);
        callContact.enqueue(new Callback<List<Buyer>>() {
            @Override
            public void onResponse(Call<List<Buyer>> call, Response<List<Buyer>> response) {

                if (response.body().size() != 0) {
                    buyer = response.body().get(0);
                    setDataToView(buyer);

//                    mnuEdit.setVisible(true);
//                    mnuDelete.setVisible(true);
                }
            }

            @Override
            public void onFailure(Call<List<Buyer>> call, Throwable t) {
                   Toast.makeText(getContext(),"Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                intent.putExtra("id", contact.getId());
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
    }

    private void setDataToView(Buyer buyer) {
        tvharga_jual.setText(buyer.getHarga_jual());
        tvCp.setText(buyer.getCp());
        tvUnit.setText(buyer.getUnit());
        tvNama.setText(buyer.getNama());
        tvNoTelp.setText(buyer.getNoTelp());
        tvno_pasangan.setText(buyer.getno_pasangan());
        tvno_darurat.setText(buyer.getno_darurat());
        tvAlamat.setText(buyer.getAlamat());
        tvPekerjaan.setText(buyer.getPekerjaan());
        tvstatus_berkas.setText(buyer.getstatus_berkas());
        tvtgl_booking.setText(buyer.gettgl_booking());
        tvbank_pel.setText(buyer.getbank_pel());
        tvKet.setText(buyer.getKet());
        a.setText(buyer.getA());
        b.setText(buyer.getB());
        c.setText(buyer.getC());
        d.setText(buyer.getD());
        e.setText(buyer.getE());
        f.setText(buyer.getF());
        g.setText(buyer.getG());
    }

}
