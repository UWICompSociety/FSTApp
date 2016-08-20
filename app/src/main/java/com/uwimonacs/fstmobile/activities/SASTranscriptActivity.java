package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.SASTranscriptAdapter;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Transcript;

import java.text.DecimalFormat;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class SASTranscriptActivity extends AppCompatActivity implements
SwipeRefreshLayout.OnRefreshListener{
    private SASConfig sasConfig;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private AccountManager manager;
    private Account account;

    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sasConfig = MyApplication.getSasConfig();
        manager = AccountManager.get(this);
        account = manager.getAccountsByType("UWI")[0];

        webView = MyApplication.getWebView();

        setContentView(R.layout.activity_sastranscript);
        if(isFirstTime()){
            //Show tutorial
            new MaterialShowcaseView.Builder(this)
                    .setTarget(findViewById(R.id.circle_target))
                    .setDismissText("GOT IT")
                    .setContentText("Swipe down to refresh your transcript")
                    .show();
        }

        Transcript transcript = sasConfig.student.getTranscript();
        TextView degreeType = (TextView) findViewById(R.id.degree_type),
                program = (TextView) findViewById(R.id.program),
                faculty = (TextView) findViewById(R.id.faculty),
                major = (TextView) findViewById(R.id.major),
                minor = (TextView) findViewById(R.id.minor);

        String sDegree = "Degree type: " + transcript.getDegree(),
                sProgram = "Program: " + transcript.getProgram(),
                sFaculty = "Faculty: " + transcript.getFaculty(),
                sMajor = "Major: " + transcript.getMajor(),
                sMinor = "Minor: " + transcript.getMinor();

        degreeType.setText(sDegree);
        program.setText(sProgram);
        faculty.setText(sFaculty);
        major.setText(sMajor);
        minor.setText(sMinor);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.transcript_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(toolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(toolbarLayout))
                    toolbarLayout.setTitle("View your transcript");
                else
                    toolbarLayout.setTitle(" ");
            }
        });

        final RecyclerView institutionCredit = (RecyclerView) findViewById(R.id.institution_credit);
        institutionCredit.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        institutionCredit.setLayoutManager(layoutManager);
        final SASTranscriptAdapter adapter = new SASTranscriptAdapter();
        sasConfig.setTranscriptAdapter(adapter);
        sasConfig.setTranscriptActivity(this);
        sasConfig.setSwipe2(swipeRefreshLayout);
        new Runnable(){
            @Override
            public void run() {
                institutionCredit.setAdapter(adapter);
            }
        }.run();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Transcript transcript = sasConfig.student.getTranscript();
        TextView cumulativeGPA = (TextView) findViewById(R.id.total_gpa),
                degreeGPA = (TextView) findViewById(R.id.degree_gpa);

        String scGPA = "Cumulative GPA: " + new DecimalFormat("0.00").format(transcript.getGPA()),
                sdGPA = "Degree GPA: " + new DecimalFormat("0.00").format(transcript.getDegreeGPA());

        cumulativeGPA.setText(scGPA);
        degreeGPA.setText(sdGPA);
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("TranscriptActivity", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("TranscriptActivity", true);
            editor.apply();
        }
        return !ranBefore;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                return true;
        }
    }

    @Override
    public void onRefresh() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.sasConfig.passiveLogin('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'transcript');");
                super.onPageFinished(view, url);
            }
        });
        String idNumber = sasConfig.student.getIdNumber();
        String password = manager.getPassword(account);
        String formData = "sid="+idNumber+"&PIN="+password;
        webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
    }
}
