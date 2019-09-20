package com.example.weibotoptabview2;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeiBoTopTabView.OnTabClickListener {
    List<Fragment> contents = new ArrayList<>();
    int lastPosition = 0;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WeiBoTopTabView tabView = findViewById(R.id.tabView);
        tabView.setOnTabClickListener(this);
        mViewPager = findViewById(R.id.mViewPager);
        contents.add(new MainFragment());
        contents.add(new NewsFragment());
        contents.add(new FriendFragment());
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(tabView);
    }

    @Override
    public void onClick(int position) {
        mViewPager.setCurrentItem(position);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return contents.get(i);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}



