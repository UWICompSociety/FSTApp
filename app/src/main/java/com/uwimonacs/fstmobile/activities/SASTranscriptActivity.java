package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.SASTranscriptAdapter;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Transcript;

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.transcript_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //        Transcript transcript = sasConfig.student.getTranscript();
//        TextView degreeType = (TextView) findViewById(R.id.degree_type),
//                program = (TextView) findViewById(R.id.program),
//                faculty = (TextView) findViewById(R.id.faculty),
//                major = (TextView) findViewById(R.id.major),
//                minor = (TextView) findViewById(R.id.minor);
//
//        String sDegree = "Degree type: " + transcript.getDegree(),
//                sProgram = "Program: " + transcript.getProgram(),
//                sFaculty = "Faculty: " + transcript.getFaculty(),
//                sMajor = "Major: " + transcript.getMajor(),
//                sMinor = "Minor: " + transcript.getMinor();
//
//        degreeType.setText(sDegree);
//        program.setText(sProgram);
//        faculty.setText(sFaculty);
//        major.setText(sMajor);
//        minor.setText(sMinor);

        final RecyclerView institutionCredit = (RecyclerView) findViewById(R.id.institution_credit);
        institutionCredit.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        institutionCredit.setLayoutManager(layoutManager);
        SASTranscriptAdapter adapter = new SASTranscriptAdapter();
        sasConfig.setTranscriptAdapter(adapter);
        sasConfig.setTranscriptActivity(this);
        sasConfig.setSwipe2(swipeRefreshLayout);
        institutionCredit.setAdapter(adapter);
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
