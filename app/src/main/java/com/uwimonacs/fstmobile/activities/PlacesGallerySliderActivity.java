package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.viewpagerindicator.CirclePageIndicator;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.GallerySliderAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Akinyele on 8/4/2017.
 */

public class PlacesGallerySliderActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static ArrayList<String> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_gallery_viewpager);

        final Bundle extras = getIntent().getExtras();
        urls = (ArrayList<String>) extras.get("URLs");
        currentPage = (int)extras.get("Position");

        init();
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.slider);
        mPager.setAdapter(new GallerySliderAdapter(PlacesGallerySliderActivity.this , urls));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES = urls.size();


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
                mPager.setCurrentItem(currentPage, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }
}
