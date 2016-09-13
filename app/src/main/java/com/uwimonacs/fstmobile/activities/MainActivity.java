package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.TabPagerAdapter;
import com.uwimonacs.fstmobile.adapters.TermsAdapter;
import com.uwimonacs.fstmobile.models.News;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Student;
import com.uwimonacs.fstmobile.models.TimeTable;

import java.util.ArrayList;
import java.util.List;

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
    private AppCompatActivity activity = this;
    private String sharedItemUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Check if activity was started by a url
        if(getIntent().getDataString() != null) {
            sharedItemUrl = getIntent().getDataString();

            List<News> newsList = new Select().all().from(News.class).execute();

            //Search for and display news story
            for(News news1: newsList){
                if(news1.getUrl().equals(sharedItemUrl) || news1.getUrl().contains(sharedItemUrl)){
                    final Intent intent = new Intent(this, NewsDetailActivity.class);
                    intent.putExtra("image", news1.getImage_url());
                    intent.putExtra("title", news1.getTitle());
                    intent.putExtra("story", news1.getStory());
                    startActivity(intent);
                    break;
                }
            }
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            final ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);

            toggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_drawer);

        setNavDrawerListener();

        if(!doesDatabaseAccountExist())
            setUpSAS();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        pager = (ViewPager)findViewById(R.id.pager);

        tabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager());

        pager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(pager);

//        Subscribe to Firebase notifications
        if(isFirstTime()){
            FirebaseMessaging instance = FirebaseMessaging.getInstance();
            instance.subscribeToTopic("news");
            instance.subscribeToTopic("alerts");
            instance.subscribeToTopic("events");
            instance.subscribeToTopic("gallery");
            instance.subscribeToTopic("schol");
        }

    }

    private boolean isFirstTime() {
        SharedPreferences preferences =
                getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("FirebaseNotifications", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("FirebaseNotifications", true);
            editor.apply();
        }
        return !ranBefore;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        if (sasConfig.student != null) {
            sasConfig.serialize();
            sasConfig.save();
            sasConfig.student.serialize();
            sasConfig.student.save();
        }
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean doesDatabaseAccountExist(){
        try {
            SASConfig sasConfig1 = (SASConfig) new Select().from(SASConfig.class).execute().get(0);
            Student student = (Student) new Select().from(Student.class).execute().get(0);
            if(student != null && sasConfig1 != null) {
                init();
                sasConfig.student = student;
                sasConfig.student.unSerialize();

                sasConfig1.unSerialize();
                sasConfig.termNames = sasConfig1.termNames;
                sasConfig.termValues = sasConfig1.termValues;

                loggedIn = true;

                fixNavDrawerForDatabase();
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private void init(){
        sasConfig = MyApplication.getSasConfig();
        sasConfig.setmActivity(this);
        webView = MyApplication.getWebView();
        mAccountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        mAccounts = mAccountManager.getAccountsByType("UWI");
        if(mAccounts.length > 0)
            mAccount = mAccounts[0];
    }

    public void setUpSAS() {
        init();
        cleanUpNavDrawer();

    }

    public void login(){
        SubMenu subMenu = navigationView.getMenu().getItem(0).getSubMenu();

        final MenuItem registration = subMenu.findItem(R.id.sas_registration);
        final MenuItem transcript = subMenu.findItem(R.id.sas_transcript);
        final MenuItem logout = subMenu.findItem(R.id.sas_logout);

        registration.setEnabled(false);
        transcript.setEnabled(false);
        logout.setEnabled(false);
        final String username = mAccount.name;
        final String password = mAccountManager.getPassword(mAccount);
        final String formData = "sid=" + username + "&PIN=" + password;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                final Student student = new Student(username);
                student.setPassword(password);
                sasConfig.student = student;
                view.loadUrl("javascript:window.sasConfig.Login('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'main');");
                super.onPageFinished(view, url);
            }
        });
        webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
    }

    public void setNavDrawerListener(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sas_registration:
                        if (!loggedIn) {
                            ImageView queensWay = (ImageView) findViewById(R.id.queens_way);
                            Pair<View, String> pair = Pair.create((View)queensWay,"queens_way");
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair);
                            Intent intent = new Intent(getApplicationContext(), SASLoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction("MainActivity");
                            if(Build.VERSION.SDK_INT >= 21)
                                startActivity(intent, options.toBundle());
                            else
                                startActivity(intent);
                        } else {
                            drawer.closeDrawers();
                            Intent intent = new Intent(getApplicationContext(), SASTimetableActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        return true;

                    case R.id.sas_transcript:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), SASTranscriptActivity.class));
                        return true;

                    case R.id.sas_logout:
                        drawer.closeDrawers();
                        removeAccountDialog();
                        return true;

                    case R.id.settings:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        return true;
                    case R.id.schedule:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), ScheduleActivity.class));
                        return true;
                    case R.id.alerts:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), QuickAlertsActivity.class));
                        return true;

                    default:
                        drawer.closeDrawers();
                        return true;
                }
            }
        });
    }

    public void removeAccountDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Logging out");

        alertDialogBuilder
                .setMessage("This will remove your account from this device and close the app. \n" +
                        "You can log in again")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        removeAccount();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Do nothing
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void removeAccount(){
        loggedIn = false;
        AccountManager manager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = manager.getAccountsByType("UWI");
        Account account = new Account("dummy", "dummy");
        Student student = sasConfig.student;
        for (final Account listItem: accounts) {
            if (student.getIdNumber().equals(listItem.name))
                account = listItem;
        }

        try {
            if (Build.VERSION.SDK_INT < 22) {
                manager.removeAccount(account, null, null);
            } else {
                manager.removeAccountExplicitly(account);
            }
        } catch (SecurityException e){
            //Fail gracefully
        }

        new Delete().from(SASConfig.class).where("ConfigID = ?", 0).execute();
        new Delete().from(Student.class).where("id_number = ?", student.getIdNumber()).execute();

        finishAffinity();
    }

    /**
     * Reset the navigation drawer to what it looked like before logging infix
     */
    public void cleanUpNavDrawer() {
        TermsAdapter terms = new TermsAdapter(this, android.R.layout.simple_spinner_item);
        terms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        terms.addAll(new ArrayList<String>());
        Spinner termSelector = (Spinner) navigationView.getHeaderView(0).findViewById(R.id.term);
        termSelector.setAdapter(terms);

        SubMenu subMenu = navigationView.getMenu().getItem(0).getSubMenu();
        subMenu.findItem(R.id.sas_registration).setTitle("Log in");
        subMenu.findItem(R.id.sas_transcript).setVisible(false);
        subMenu.findItem(R.id.sas_logout).setVisible(false);
    }

    public void fixNavDrawerForDatabase(){
        View header = navigationView.getHeaderView(0);

        final TextView username = (TextView) header.findViewById(R.id.username),
                idNumber = (TextView) header.findViewById(R.id.id_number);

        username.setText(sasConfig.student.getName());
        idNumber.setText(sasConfig.student.getIdNumber());

        TermsAdapter terms = new TermsAdapter(this, android.R.layout.simple_spinner_item);
        terms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        terms.addAll(sasConfig.termNames);
        Spinner termSelector = (Spinner) header.findViewById(R.id.term);
        termSelector.setOnItemSelectedListener(this);
        termSelector.setAdapter(terms);
        sasConfig.setTerms(terms);

        final MenuItem registration = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_registration);
        final MenuItem transcript = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript);
        final MenuItem logout = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout);

        registration.setEnabled(true);
        transcript.setEnabled(true);
        logout.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        /*
        * A new term has been selected, send a post request to set the change
        * and invoke a JS callback to get all the courses for that term
        * */
        final String term = sasConfig.termValues.get(pos);

        TimeTable timeTable = sasConfig.student.getTimeTable(term);
        if (timeTable != null) {
            //We already have the timetable
            sasConfig.selectTerm(term);
            sasConfig.student.setTimeTable(timeTable);
            final Menu menu = navigationView.getMenu();
            final MenuItem viewTranscript = menu.getItem(0).getSubMenu().findItem(R.id.sas_transcript);
            viewTranscript.setEnabled(true);
        } else if (!term.equals(sasConfig.term)) {
            //We don't have the timetable we need to get it
            final Menu menu = navigationView.getMenu();
            final MenuItem item = menu.getItem(0).getSubMenu().getItem(0);
            item.setEnabled(false);
            item.setActionView(new ProgressBar(this));

            sasConfig.selectTerm(term);

            String formData;
            try {
                formData = "sid=" + sasConfig.student.getIdNumber() + "&PIN=" + mAccountManager.getPassword(mAccount);
            } catch (IllegalArgumentException e) {
                formData = "sid=" + sasConfig.student.getIdNumber() + "&PIN=" + sasConfig.student.getPassword();
            }
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    // TODO: Fetch all courses for the current semester
                    view.loadUrl("javascript:window.sasConfig.passiveLogin('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'timetable');");
                    super.onPageFinished(view, url);
                }
            });
            webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }
}
