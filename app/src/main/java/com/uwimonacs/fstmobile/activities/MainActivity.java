package com.uwimonacs.fstmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.TabPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabPagerAdapter tabPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        pager = (ViewPager)findViewById(R.id.pager);

        tabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager());

        pager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_videos:
                Intent intent = new Intent(this, VideoListActivity.class);
                startActivity(intent);
                return true;

            //navigate to FAQ activity
            case R.id.action_faqs:
                Intent faqIntent = new Intent(this, FAQActivity.class);
                startActivity(faqIntent);
                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
