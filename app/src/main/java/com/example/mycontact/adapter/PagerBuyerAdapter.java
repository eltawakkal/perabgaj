package com.example.mycontact.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mycontact.fragments.FragUnVerifiedBuyer;
import com.example.mycontact.fragments.FragVerifiedBuyer;

public class PagerBuyerAdapter extends FragmentPagerAdapter {

    private String houseId;

    private FragVerifiedBuyer fragVerifiedBuyer;
    private FragUnVerifiedBuyer fragUnVerifiedBuyer;

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    public PagerBuyerAdapter(FragmentManager fm, String houseId) {
        super(fm);
        this.houseId = houseId;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Sudah Akad Kredit";
        } else if (position == 1) {
            return "Belum Akad Kredit";
        }

        return null;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            fragVerifiedBuyer = new FragVerifiedBuyer();
            fragVerifiedBuyer.setHouseId(houseId);
            return fragVerifiedBuyer;
        } else if (position == 1) {
            fragUnVerifiedBuyer = new FragUnVerifiedBuyer();
            fragUnVerifiedBuyer.setHouseId(houseId);
            return fragUnVerifiedBuyer;
        }

        FragVerifiedBuyer fragVerifiedBuyer = new FragVerifiedBuyer();
        fragVerifiedBuyer.setHouseId(houseId);
        return fragVerifiedBuyer;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void setFragActivity(int postion) {
        if (postion == 0) {
            fragVerifiedBuyer.setActivity();
        } else if (postion == 1) {
            fragUnVerifiedBuyer.setActivity();
        }
    }
}
