package com.example.mycontact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mycontact.adapter.PagerBuyerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class BuyerActivity extends AppCompatActivity {

    private BuyerListener buyerListener;
    private SearchView searchView;

    private FloatingActionButton fabAdd;

    public interface BuyerListener {
        void onBuyerSearchChange(String buyer);
    }

//    variables
    public static final String HOUSE_ID_KEY = "house_id";
    private String houseId;
    private String houseName;

//    views
    private TabLayout tabLayout;
    private ViewPager pager;
    private Toolbar toolbarMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_container);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searcMenuItem = menu.findItem(R.id.action_cari);
        searchView = (SearchView) MenuItemCompat.getActionView(searcMenuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buyerListener.onBuyerSearchChange(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_transaction) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(HOUSE_ID_KEY, houseId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        houseId = getIntent().getStringExtra(HouseActivity.ID_KEY);
        houseName = getIntent().getStringExtra(HouseActivity.NAME_KEY);

        toolbarMain = findViewById(R.id.toolbar_main);
        fabAdd = findViewById(R.id.fab_add_buyer);


        setSupportActionBar(toolbarMain);
        setTitle(houseName);

        PagerBuyerAdapter pagerAdapter = new PagerBuyerAdapter(getSupportFragmentManager(), houseId);

        tabLayout = findViewById(R.id.tab_buyer);
        pager = findViewById(R.id.pager_buyer);

        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                searchView.setQuery("", true);
                toolbarMain.collapseActionView();
                pagerAdapter.setFragActivity(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        toolbarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = new Intent(this, AddEditActivity.class);
        intent.putExtra(HOUSE_ID_KEY, houseId);
        fabAdd.setOnClickListener(v -> startActivity(intent));
    }

    public void setBuyerListener(BuyerListener buyerListener) {
        this.buyerListener = buyerListener;
    }
}
