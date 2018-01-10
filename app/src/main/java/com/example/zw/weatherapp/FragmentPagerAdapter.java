package com.example.zw.weatherapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by zw on 2016/9/26.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter{
    ArrayList<WeatherFragment>list;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<WeatherFragment> list) {
        super(fm);
        this.list = list;
    }




    @Override
    public Fragment getItem(int position) {
      return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
