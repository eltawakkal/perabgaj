package com.example.mycontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.mycontact.fragments.FragMutation;
import com.example.mycontact.fragments.FragTagihan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

    private BottomNavigationView bnvTransaction;

    private String houseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        initView();
    }

    private void initView() {
        bnvTransaction = findViewById(R.id.bnv_transaction);

        houseId = getIntent().getStringExtra(BuyerActivity.HOUSE_ID_KEY);

        Bundle bundle = new Bundle();
        bundle.putString(BuyerActivity.HOUSE_ID_KEY, houseId);

        FragMutation fragMutation = new FragMutation();
        FragTagihan fragTagihan = new FragTagihan();

        fragMutation.setArguments(bundle);
        fragTagihan.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_trans_container, fragMutation).commit();

        bnvTransaction.setOnNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            if (item.getItemId() == R.id.menu_mutation) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_trans_container, fragMutation).commit();
            } else if (item.getItemId() == R.id.menu_tagihan) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_trans_container, fragTagihan).commit();
            }

            return false;
        });
    }

}
