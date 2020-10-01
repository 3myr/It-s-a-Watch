package com.example.ItsAWatch.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    // ATTRIBUTS

    private final List<Fragment> mFragmentList = new ArrayList<>();

    //

    // CONSTRUCTEUR

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, Lifecycle b ) {
        super(fragmentManager,b);
    }

    //

    // GETTER / SETTER

    @Override
    public int getItemCount() {
        return mFragmentList.size();
    }


    //

    // PROCEDURES

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragmentList.get(position);
    }

    //

}
