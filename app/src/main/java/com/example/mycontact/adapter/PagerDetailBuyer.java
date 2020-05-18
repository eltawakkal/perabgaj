package com.example.mycontact.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mycontact.DetailActivity;
import com.example.mycontact.fragments.FragDetailBuyerData;
import com.example.mycontact.fragments.FragDetailBuyerPayment;
import com.example.mycontact.model.Buyer;

public class PagerDetailBuyer extends FragmentStatePagerAdapter {

    private Buyer buyer;

    public PagerDetailBuyer(@NonNull FragmentManager fm, Buyer buyer) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        this.buyer = buyer;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Data";
        } else if (position == 1) {
            return "Pembayaran";
        }

        return "Data";
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            FragDetailBuyerData fragDetailBuyerData = new FragDetailBuyerData();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailActivity.BUYER_KEY, buyer);
            fragDetailBuyerData.setArguments(bundle);
            return fragDetailBuyerData;
        } else if (position == 1) {
            FragDetailBuyerPayment fragDetailBuyerPayment = new FragDetailBuyerPayment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailActivity.BUYER_KEY, buyer);
            fragDetailBuyerPayment.setArguments(bundle);
            return fragDetailBuyerPayment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
