package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

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
    private AppCompatActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

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

        setUpSAS();

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        pager = (ViewPager)findViewById(R.id.pager);

        tabPagerAdapter = new TabPagerAdapter(this.getSupportFragmentManager(), this);

        pager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(pager);

    }

    @Override
    protected void onResume() {
        if (sasConfig.termNames.size() == 0 && loggedIn) {
            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setTitle("View timetable");
            setUpSAS();
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript).setVisible(true);
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout).setVisible(true);
        }
        super.onResume();
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

    public void setUpSAS() {
        sasConfig = MyApplication.getSasConfig();
        sasConfig.setmActivity(this);
        webView = MyApplication.getWebView();
        mAccountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        mAccounts = mAccountManager.getAccountsByType("UWI");
        navigationView = (NavigationView) findViewById(R.id.nav_drawer);

        final MenuItem registration = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_registration);
        final MenuItem transcript = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript);
        final MenuItem logout = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout);
        assert navigationView != null;

        if (mAccounts.length > 0) {
            registration.setEnabled(false);
            transcript.setEnabled(false);
            logout.setEnabled(false);
            mAccount = mAccounts[0];
            //Login with single account
            final String username = mAccount.name;
            final String password = mAccountManager.getPassword(mAccount);
            final String formData = "sid=" + username + "&PIN=" + password;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
            });
        } else {
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_registration).setTitle("Log in");
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript).setVisible(false);
            navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout).setVisible(false);
        }
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
                        removeAccount();
                        return true;

                    case R.id.settings:
                        drawer.closeDrawers();
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        return true;

                    default:
                        drawer.closeDrawers();
                        return true;
                }
            }
        });
    }

    public void removeAccount() {
        final AppCompatActivity activity = this;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle("Logging out");

        alertDialogBuilder
                .setMessage("This will remove your account from this device. \n" +
                        "You can log in again")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        loggedIn = false;
                        AccountManager manager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
                        Account[] accounts = manager.getAccountsByType("UWI");
                        Account account = new Account("dummy", "dummy");
                        for (final Account listItem: accounts) {
                            if (sasConfig.student.getIdNumber().equals(listItem.name))
                                account = listItem;
                        }

                        if (Build.VERSION.SDK_INT < 22) {
                            manager.removeAccount(account, null, null);
                        } else {
                            manager.removeAccount(account, activity, null, null);
                        }

                        sasConfig.student = null;
                        //ActivityCompat.finishAffinity(activity);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cleanUp();
                            }
                        });
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

    public void cleanUp(){
        ((TextView)findViewById(R.id.username)).setText("SAS User");
        ((TextView)findViewById(R.id.id_number)).setText("620012345");
        sasConfig.getTerms().clear();
        navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_registration).setTitle("Log in");
        navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript).setVisible(false);
        navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout).setVisible(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        /*
        * A new term has been selected, send a post request to set the change
        * and invoke a JS callback to get all the courses for that term
        * */
        final String term = sasConfig.termValues.get(pos);
        if (!term.equals(sasConfig.term)) {
            final Menu menu = navigationView.getMenu();
            final MenuItem item = menu.getItem(0).getSubMenu().getItem(0);
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
