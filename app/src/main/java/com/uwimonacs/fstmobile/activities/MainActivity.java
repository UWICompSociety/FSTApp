package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.TabPagerAdapter;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Student;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity
implements AdapterView.OnItemSelectedListener{
    private TabPagerAdapter tabPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager pager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SASConfig sasConfig;
    private WebView webView;
    public static Account[] mAccounts;
    private Account mAccount;
    private AccountManager mAccountManager;
    public static boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
            toggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        setUpSAS();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        pager = (ViewPager)findViewById(R.id.pager);

        tabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager());

        pager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(pager);

    }

    @Override
    protected void onResume() {
        if(sasConfig.termNames.size() == 0 && loggedIn) {
            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setTitle("View timetable");
            setUpSAS();
            navigationView.getMenu().getItem(0).getSubMenu().getItem(1).setVisible(true);
        }
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setUpSAS(){
        sasConfig = MyApplication.getSasConfig();
        sasConfig.setmActivity(this);
        webView = MyApplication.getWebView();
        mAccountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        mAccounts = mAccountManager.getAccountsByType("UWI");
        navigationView = (NavigationView) findViewById(R.id.nav_drawer);
        assert navigationView != null;
        if(mAccounts.length > 0) {
            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(0).getSubMenu().getItem(1).setEnabled(false);
            mAccount = mAccounts[0];
            //Login with single account
            final String username = mAccount.name;
            final String password = mAccountManager.getPassword(mAccount);
            final String formData = "sid="+username+"&PIN="+password;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.setWebViewClient(new WebViewClient(){
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            Student student = new Student(username);
                            student.setPassword(password);
                            sasConfig.student = student;
                            view.loadUrl("javascript:window.sasConfig.Login('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'main');");
                            super.onPageFinished(view, url);
                        }
                    });
                    webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
                }
            });
        } else {
            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setTitle("Log in");
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawer.closeDrawers();
                switch(item.getItemId()){
                    case R.id.sas_registration:
                        if(!loggedIn) {
                            Intent intent = new Intent(getApplicationContext(), SASLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(getApplicationContext(), SASTimetableActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        return true;
                    // Including legal notices as an independent menu item,
                    // or as part of an "About" menu item, is recommended.
                    case R.id.legal_notices:
                        startActivity(new Intent(getApplicationContext(), LegalNoticesActivity.class));
                        return true;
                    case R.id.settings:
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        /*
        * A new term has been selected, send a post request to set the change
        * and invoke a JS callback to get all the courses for that term
        * */
        final String term = sasConfig.termValues.get(pos);
        if(!term.equals(sasConfig.term)) {
            Menu menu = navigationView.getMenu();
            MenuItem item = menu.getItem(0).getSubMenu().getItem(0);
            item.setEnabled(false);
            sasConfig.selectTerm(term);
            final String formData = "name_var=bmenu.P_RegMnu&term_in=" + term;
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    System.out.println("Term selected: " + term);
                    // TODO: Fetch all courses for the current semester
                    view.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            view.loadUrl("javascript:window.sasConfig.loadCourses('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
                            super.onPageFinished(view, url);
                        }
                    });
                    view.loadUrl(getResources().getString(R.string.detailschedule_get));
                    super.onPageFinished(view, url);
                }
            });
            webView.postUrl("http://sas.uwimona.edu.jm:9010/banndata1-srv_mona/bwcklibs.P_StoreTerm", formData.getBytes());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }
}
