package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.SASConfig;
import com.uwimonacs.fstmobile.models.Student;

@SuppressLint("NewApi")
public class SASLoginActivity extends AccountAuthenticatorActivity {
    private SASConfig sasConfig;
    private WebView webView;
    private AccountManager mAccountManager;
    public static boolean main;
    public static Account[] mAccounts;
    private Account mAccount;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_saslogin);

        login_help = (TextView) findViewById(R.id.login_help);

        login_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog(SASLoginActivity.this);
            }
        });

        sasConfig = MyApplication.getSasConfig();
        webView = MyApplication.getWebView();
        mAccountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        Intent intent = getIntent();
        try {
            main = intent.getAction().equals(Intent.ACTION_MAIN);
        } catch (NullPointerException e){
            main = false;
        }
        mAccounts = mAccountManager.getAccountsByType("UWI");
        System.out.println("Accounts: " + mAccounts.length);
        if(mAccounts.length == 0){
            //Show login layout
            tryLogin(0);
        }
        else if(mAccounts.length == 1) {
            if(main) {
                mAccount = mAccounts[0];
                tryLogin(1); //Login with single account
            }
            else
                tryLogin(0); //Allow new account to be added
        }
        else if(mAccounts.length > 1){
            if(main)
                showAccountChooser(); //Login with chosen account
            else
                tryLogin(0); //Allow new account to be added
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Login with chosen account
        try {
            Bundle bundle = data.getExtras();
            String name = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
            assert name != null;
            for(Account account: mAccounts){
                if (name.equals(account.name)) {
                    mAccount = account;
                    break;
                }
            }
            tryLogin(1);
        } catch (RuntimeException e){
            finishAffinity();
        }
    }

    private void showAccountChooser(){
        /*
        * Show account chooser dialog
        * then login with chosen account
        * */
        Intent intent;
        if (Build.VERSION.SDK_INT < 23) {
            intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"UWI"}, false,
                    "Choose your UWI account", null, null, null);
        } else {
            intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"UWI"},
                    "Choose your UWI account", null, null, null);
        }
        startActivityForResult(intent, 1);
    }

    /**
     * Display some text in a toast for length short
     * @param message text to be displayed in toast
     */
    private void showMessage(String message) {
        Snackbar.make((LinearLayout) findViewById(R.id.sas_login_mainlayout), message, Snackbar.LENGTH_SHORT).show();
    }

    private void showHelpDialog(Context context) {
        final String MITS_HELPDESK = "https://support.mona.uwi.edu/";
        final String message = "Student users should note that your password is now the same as"
                + " your OURVLE/DOMAIN password. If your OURVLE/DOMAIN password is your date of"
                + " birth, please use the format YYYYMMDD. e.g. John Brown is a student with id"
                + " number 89876543. John was born on January 3, 1989. In this case John would"
                + " enter: 89876543 in the slot for User ID and, 19890103 in the slot for Password."
                + "\n\n"
                + "If you do not remember your OURVLE/DOMAIN password or it has expired, please"
                + " contact the MITS Helpdesk at extension 2992 or (876) 927-2148. You may also"
                + " email the helpdesk or visit the UWI Mona Live Support page to  request a "
                + "password reset";

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Login Help").setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * Checks for an available network connection
     * @return true if a network connection is detected, false otherwise
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Shows an alert dialog when a network connection is unavailable
     * and offers to enable wifi
     */
    private void showNetworkDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("No network connection");

        // set dialog message
        final AccountAuthenticatorActivity activity = this;
        alertDialogBuilder
                .setMessage("Enable WIFI?")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        //enable wifi
                        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activity.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void createAccount(final String username, final String password){
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
                        view.loadUrl("javascript:window.sasConfig.Login('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'login');");
                        super.onPageFinished(view, url);
                    }
                });
                webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
            }
        });
    }

    private void tryLogin(int numberOfAccounts) {
        if (numberOfAccounts == 0) {
            final RelativeLayout progressBar = (RelativeLayout) findViewById(R.id.sas_login_progressbar);
            assert progressBar != null;
            final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.sas_login_mainlayout);
            assert mainLayout != null;
            sasConfig.setLogin(this);
            final EditText id = (EditText) findViewById(R.id.sas_username_edittext);
            final EditText pass = (EditText) findViewById(R.id.sas_password_edittext);
            Button button = (Button) findViewById(R.id.sas_login_button);
            assert button != null;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    id.setError(null);
                    pass.setError(null);

                    final String username = id.getText().toString();
                    final String password = pass.getText().toString();

                    if (TextUtils.isEmpty(username)) {
                        id.setError("ID number required");
                        showMessage("ID Number field required");
                    }
                    else if (username.length() != 9) {
                        id.setError("ID number is too short");
                        showMessage("ID number is too short");
                    }
                    else if (TextUtils.isEmpty(password)) {
                        pass.setError("Password required");
                        showMessage("Password field required");
                    }
                    else if (!isNetworkAvailable())
                        showNetworkDialog();
                    else {
                        mainLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        createAccount(username, password);
                    }
                }
            });
        } else if (!isNetworkAvailable())
            showNetworkDialog();
        else {
            LinearLayout main = (LinearLayout) findViewById(R.id.sas_login_mainlayout);
            RelativeLayout progressBar = (RelativeLayout) findViewById(R.id.sas_login_progressbar);
            main.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Account account = mAccount;
            final String username = account.name;
            final String password = mAccountManager.getPassword(account);
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
                            view.loadUrl("javascript:window.sasConfig.Login('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'login');");
                            super.onPageFinished(view, url);
                        }
                    });
                    webView.postUrl(getResources().getString(R.string.login_post),
                            formData.getBytes());
                }
            });
        }
    }

    private TextView login_help;
}
