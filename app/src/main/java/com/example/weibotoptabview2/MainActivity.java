package com.example.weibotoptabview2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;


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

   //  this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        NavigationBarStatusBar(this,hasFocus);
    }

    /**
     * 导航栏，状态栏隐藏
     * @param activity
     */
    public static void NavigationBarStatusBar(Activity activity, boolean hasFocus){
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        //activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        //activity.getWindow().setStatusBarColor(Color.TRANSPARENT);

    }

}



