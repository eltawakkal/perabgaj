package com.example.mycontact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mycontact.model.Buyer;
import com.example.mycontact.model.Transaction;
import com.example.mycontact.network.ApiClient;
import com.example.mycontact.network.ApiEndPoint;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditActivity extends AppCompatActivity {

//    objects
    private ApiEndPoint apiEndPoint;
    private Buyer buyer;

//    variables
    private int action;
    private String houseId;

//    views
    private EditText edtharga_jual;
    private EditText edtCp;
    private EditText edtNama;
    private EditText edtUnit;
    private EditText edtNoTelp;
    private EditText edtno_pasangan;
    private EditText edtno_darurat;
    private EditText edtAlamat;
    private EditText edtPekerjaan;
    private EditText edtstatus_berkas;
    private EditText edttgl_booking;
    private EditText edtbank_pel;
    private EditText edtKet;
    private EditText edtTglSp3k;
    private EditText b;
    private EditText c;
    private EditText d;
    private EditText e;
    private EditText f;
    private EditText g;
    private Spinner spinnerVerified;
    private MaterialButton btSaveData;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        initView();

        btSaveData.setOnClickListener(view -> {
            Buyer buyer = new Buyer(
                    null,
                    edtharga_jual.getText().toString(),
                    edtCp.getText().toString(),
                    edtUnit.getText().toString(),
                    edtNama.getText().toString(),
                    houseId,
                    edtNoTelp.getText().toString(),
                    edtno_pasangan.getText().toString(),
                    edtno_darurat.getText().toString(),
                    edtAlamat.getText().toString(),
                    edtPekerjaan.getText().toString(),
                    edtstatus_berkas.getText().toString(),
                    edttgl_booking.getText().toString(),
                    edtbank_pel.getText().toString(),
                    edtKet.getText().toString(),
                    edtTglSp3k.getText().toString(),
                    b.getText().toString(),
                    c.getText().toString(),
                    d.getText().toString(),
                    e.getText().toString(),
                    f.getText().toString(),
                    g.getText().toString(),
                    spinnerVerified.getSelectedItem().toString()
            );

            if (action == 0) {
                saveDataToServer(buyer);
            } else {
                updateDataToServer(buyer);
            }
        });
    }

    private void initView() {
        apiEndPoint = ApiClient.getRetrofit().create(ApiEndPoint.class);

        toolbar = findViewById(R.id.toolbar_add_edit);
        edtharga_jual = findViewById(R.id.edtharga_jual);
        edtCp = findViewById(R.id.edtCp);
        edtUnit = findViewById(R.id.edt_unit);
        edtNama = findViewById(R.id.edt_nama);
        edtNoTelp = findViewById(R.id.edt_no_telp);
        edtno_pasangan = findViewById(R.id.edt_no_pasangan);
        edtno_darurat = findViewById(R.id.edt_no_darurat);
        edtAlamat = findViewById(R.id.edt_alamat);
        edtPekerjaan = findViewById(R.id.edt_pekerjaan);
        edtstatus_berkas = findViewById(R.id.edt_sts_berkas);
        edttgl_booking = findViewById(R.id.edt_tgl_booking);
        edtbank_pel = findViewById(R.id.edt_bank_pel);
        edtKet = findViewById(R.id.edt_ket);
        edtTglSp3k = findViewById(R.id.edt_tgl_sp3k);
        b = findViewById(R.id.edt_b);
        c = findViewById(R.id.edt_c);
        d = findViewById(R.id.edt_d);
        e = findViewById(R.id.edt_e);
        f = findViewById(R.id.edt_f);
        g = findViewById(R.id.edt_g);
        spinnerVerified = findViewById(R.id.spin_verified);
        btSaveData = findViewById(R.id.mtbt_simpan);

        edttgl_booking.setOnClickListener(v -> { pickDate(edttgl_booking); });
        edtTglSp3k.setOnClickListener(v -> { pickDate(edtTglSp3k); });
        d.setOnClickListener(v -> { pickDate(d); });

        setSupportActionBar(toolbar);
        setTitle("Tambah Data Baru");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        action = getIntent().getIntExtra("action", 0);
        houseId = getIntent().getStringExtra(BuyerActivity.HOUSE_ID_KEY);

        if (action == 1) {
            btSaveData.setText("Perbarui");
            edtUnit.setEnabled(false);
            buyer = getIntent().getParcelableExtra("contact");

            setDataToView();
            setTitle("Edit Data Konsumen");

        }
    }

    private void setDataToView() {
        edtharga_jual.setText(buyer.getHarga_jual());
        edtCp.setText(buyer.getCp());
        edtUnit.setText(buyer.getUnit());
        edtNama.setText(buyer.getNama());
        edtNoTelp.setText(buyer.getNoTelp());
        edtno_pasangan.setText(buyer.getno_pasangan());
        edtno_darurat.setText(buyer.getno_darurat());
        edtAlamat.setText(buyer.getAlamat());
        edtPekerjaan.setText(buyer.getPekerjaan());
        edtstatus_berkas.setText(buyer.getstatus_berkas());
        edttgl_booking.setText(buyer.gettgl_booking());
        edtbank_pel.setText(buyer.getbank_pel());
        edtKet.setText(buyer.getKet());
        edtTglSp3k.setText(buyer.getA());
        b.setText(buyer.getB());
        c.setText(buyer.getC());
        d.setText(buyer.getD());
        e.setText(buyer.getE());
        f.setText(buyer.getF());
        g.setText(buyer.getG());

        if (buyer.getVerified().equalsIgnoreCase("Sudah Akad Kredit")) {
            spinnerVerified.setSelection(0);
        } else {
            spinnerVerified.setSelection(1);
        }

    }

    private void saveDataToServer(Buyer buyer) {
        Call<Transaction> callSaveData = apiEndPoint.addBuyer(
                buyer.getHarga_jual(),
                buyer.getCp(),
                buyer.getUnit(),
                buyer.getHouseId(),
                buyer.getNama(),
                buyer.getNoTelp(),
                buyer.getno_pasangan(),
                buyer.getno_darurat(),
                buyer.getAlamat(),
                buyer.getPekerjaan(),
                buyer.getstatus_berkas(),
                buyer.gettgl_booking(),
                buyer.getbank_pel(),
                buyer.getKet(),
                buyer.getA(),
                buyer.getB(),
                buyer.getC(),
                buyer.getD(),
                buyer.getE(),
                buyer.getF(),
                buyer.getG(),
                buyer.getVerified()

        );
        callSaveData.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                Toast.makeText(AddEditActivity.this, "Data Berhasil Disimpan", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Toast.makeText(AddEditActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(AddEditActivity.this, "Terjadi Kesalahan atau ID Unit Sudah Ada!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDataToServer(Buyer buyer) {
        Toast.makeText(this, " " + buyer.getNama(), Toast.LENGTH_SHORT).show();
        Call<Transaction> callSaveData = apiEndPoint.updateBuyer(
                buyer.getHarga_jual(),
                buyer.getCp(),
                buyer.getUnit(),
                buyer.getNama(),
                buyer.getNoTelp(),
                buyer.getno_pasangan(),
                buyer.getno_darurat(),
                buyer.getAlamat(),
                buyer.getPekerjaan(),
                buyer.getstatus_berkas(),
                buyer.gettgl_booking(),
                buyer.getbank_pel(),
                buyer.getKet(),
                buyer.getA(),
                buyer.getB(),
                buyer.getC(),
                buyer.getD(),
                buyer.getE(),
                buyer.getF(),
                buyer.getG(),
                buyer.getVerified()

        );
        callSaveData.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                Toast.makeText(AddEditActivity.this, "Data Berhasil Di Update", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("unit", buyer.getUnit());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Toast.makeText(AddEditActivity.this, "Jaringan Tidak Ditemukan , Tolong Periksa Internet Anda @PrimaGroup", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void pickDate(EditText edt) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                edt.setText(year + "-"  + (month+1) + "-" + dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}
