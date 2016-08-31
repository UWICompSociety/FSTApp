package com.uwimonacs.fstmobile.models;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.http.SslError;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.MainActivity;
import com.uwimonacs.fstmobile.activities.SASLoginActivity;
import com.uwimonacs.fstmobile.activities.SASTimetableActivity;
import com.uwimonacs.fstmobile.activities.SASTranscriptActivity;
import com.uwimonacs.fstmobile.adapters.SASTranscriptAdapter;
import com.uwimonacs.fstmobile.adapters.TermsAdapter;
import com.uwimonacs.fstmobile.models.Transcript.Term;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Holds  session data for connecting to SAS website
 */
@Table(name = "SASConfig")
public class SASConfig extends Model {
    public Student student;
    public Context context;
    public String term = "";
    private Resources resources;

    @Column(name = "term_values")
    private String serializedTermValues;

    @Column(name = "term_names")
    private String serializedTermNames;

    @Column(name = "ConfigID", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int id = 0;

    public List<String> termValues;
    public List<String> termNames;
    private WebView webView;
    private AppCompatActivity mActivity;
    private AccountAuthenticatorActivity login;
    private AccountManager mAccountManager;
    private TermsAdapter terms;
    private SASTranscriptAdapter transcriptAdapter;
    private SASTranscriptActivity transcriptActivity;
    private SASTimetableActivity timetableActivity;
    private SwipeRefreshLayout swipe1,swipe2;

    public SASConfig(){
        super();
    }

    /**
     * Initializes  variables for the first time
     * @param resources the application's current non-code resources.
     *                  Can pass getResources() from Activity class
     * @param context application context. Pass getContext() or
     *                getApplicationContext() if calling from an activity
     */
    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void initialize(Resources resources, Context context){
        this.resources = resources;
        this.webView = MyApplication.getWebView();
        this.context = context;
        termValues = new ArrayList<>();
        termNames = new ArrayList<>();
    }

    public void serialize(){
        serializedTermNames = new Gson().toJson(termNames);
        serializedTermValues = new Gson().toJson(termValues);
    }

    public void unSerialize(){
        termNames = new Gson().fromJson(serializedTermNames, new TypeToken<List<String>>(){}.getType());
        termValues = new Gson().fromJson(serializedTermValues, new TypeToken<List<String>>(){}.getType());
    }

    /**
     * Sends HTTP POST Request with login information to
     * SAS Website to initiate a session
     */
    @JavascriptInterface
    public void Login(String body, String activity){
        System.out.println(body);
        if(body.contains("Authorization Failure") || body.contains("Invalid login information")) {
            // Login failed - handle error

            if(activity.equals("login")) {
                //Simple case of invalid ID Number or Password
                Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                login.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        login.findViewById(R.id.sas_login_progressbar).setVisibility(View.GONE);
                        login.findViewById(R.id.sas_login_mainlayout).setVisibility(View.VISIBLE);
                    }
                });
            } else {
                //Credentials have changed - Let user sign in again
                Toast.makeText(context, "Your password has changed", Toast.LENGTH_SHORT).show();
                login.startActivity(new Intent(context, SASLoginActivity.class));
            }
        } else if(body.contains("Webpage not available") || body.contains("ERR_TIMED_OUT")){
            // Network timeout - handle error
            Toast.makeText(context, "Login failed. Check your internet connection and try again",
                    Toast.LENGTH_SHORT).show();
            login.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login.findViewById(R.id.sas_login_progressbar).setVisibility(View.GONE);
                    login.findViewById(R.id.sas_login_mainlayout).setVisibility(View.VISIBLE);
                }
            });
        } else {
            //Login successful - fetch user data and store account

            Document document = Jsoup.parse(body);
            Element p;
            try {
                p = document.getElementsByClass("text-intro").get(0);
            } catch(Exception e){
                // Some error page has loaded - login was not successful; Retry
                login.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String formData = "sid=" + student.getIdNumber() + "&PIN=" + student.getPassword();
                        webView.setWebViewClient(new WebViewClient(){
                            @Override
                            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                handler.proceed(); // Ignore SSL certificate errors
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                view.loadUrl("javascript:window.sasConfig.Login('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'login');");
                                super.onPageFinished(view, url);
                            }
                        });
                        webView.postUrl(resources.getString(R.string.login_post), formData.getBytes());
                    }
                });
                return;
            }
            String name = p.text().substring(5, p.text().indexOf("Student ID#:"));
            student.setName(name);

            MainActivity.loggedIn = true;
            Toast.makeText(context, "Account added", Toast.LENGTH_SHORT).show();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(termNames.size() == 0 || termValues.size() == 0) {
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public void onPageFinished(WebView view, String url) {
                                view.loadUrl("javascript:window.sasConfig.getTerms('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
                                super.onPageFinished(view, url);
                            }
                        });
                        webView.loadUrl(resources.getString(R.string.get_terms_url));
                    }
                }
            });

            addAccount();
            fixNavDrawerAfterLogin();
        }
    }

    private void addAccount(){
        String accountType = "UWI";
        String username = student.getIdNumber();
        String password = student.getPassword();
        Account sasAccount = new Account(username, accountType);
        mAccountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        mAccountManager.addAccountExplicitly(sasAccount, password, null);

//        final Intent intent = new Intent();
//        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
//        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
//        intent.putExtra(AccountManager.KEY_AUTHTOKEN, password);
//        login.setAccountAuthenticatorResult(intent.getExtras());
//        login.setResult(SASLoginActivity.RESULT_OK, intent);
        login.finish();
    }

    @JavascriptInterface
    public void passiveLogin(String body, String data){
        if(body.contains("Authorization Failure") || body.contains("Invalid login information")) {
            // Login failed - handle error

            //Credentials have changed - Let user sign in again
            Toast.makeText(context, "Your password has changed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SASLoginActivity.class);
            intent.setAction("MainActivity");
            mActivity.startActivity(intent);

        } else {
            //Login successful - fetch user data
            switch (data){
                case "timetable":
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final String formData = "name_var=bmenu.P_RegMnu&term_in=" + term;
                            webView.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    // TODO: Fetch all courses for the current semester
                                    view.setWebViewClient(new WebViewClient() {
                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            System.out.println("Loading courses");
                                            view.loadUrl("javascript:window.sasConfig.loadCourses('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
                                            super.onPageFinished(view, url);
                                        }
                                    });
                                    view.loadUrl(resources.getString(R.string.detailschedule_get));
                                    super.onPageFinished(view, url);
                                }
                            });
                            webView.postUrl("http://sas.uwimona.edu.jm:9010/banndata1-srv_mona/bwcklibs.P_StoreTerm", formData.getBytes());
                        }
                    });
                    break;
                case "transcript":
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.setWebViewClient(new WebViewClient(){
                                @Override
                                public void onPageFinished(WebView view, String url) {
                                    view.loadUrl("javascript:window.sasConfig.loadTranscript('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
                                    super.onPageFinished(view, url);
                                }
                            });
                            String formData = "levl=UG&tprt=WEB";
                            webView.postUrl(resources.getString(R.string.transcript_post), formData.getBytes());
                        }
                    });
                    break;
            }

        }
    }

    public void fixNavDrawerAfterLogin(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NavigationView navigationView = (NavigationView) mActivity.findViewById(R.id.nav_drawer);

                View header = navigationView.getHeaderView(0);

                final TextView username = (TextView) header.findViewById(R.id.username),
                        idNUmber = (TextView) header.findViewById(R.id.id_number);

                username.setText(student.getName());
                idNUmber.setText(student.getIdNumber());

                final MenuItem registration = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_registration);
                final MenuItem transcript = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript);
                final MenuItem logout = navigationView.getMenu().getItem(0).getSubMenu().findItem(R.id.sas_logout);

                registration.setTitle("View timetable");
                registration.setEnabled(false);
                registration.setActionView(new ProgressBar(mActivity));
                transcript.setVisible(true);
                logout.setVisible(true);
                transcript.setEnabled(false);
                transcript.setActionView(new ProgressBar(mActivity));
                logout.setEnabled(false);
            }
        });
    }

    @JavascriptInterface
    public void getTerms(String body) {
        Document documentBody = Jsoup.parse(body);
        Elements options = documentBody.getElementsByTag("option");
        for(int i=0; i<options.size(); i++){
            termValues.add(options.get(i).attr("value"));
            termNames.add(options.get(i).text());
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                terms = new TermsAdapter(mActivity, android.R.layout.simple_spinner_item);
                terms.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                terms.addAll(termNames);

                Spinner termSelector = (Spinner) mActivity.findViewById(R.id.term);
                assert termSelector != null;
                termSelector.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) mActivity);
                termSelector.setAdapter(terms);
            }
        });
    }

    public TermsAdapter getTerms() {
        return terms;
    }

    public void setTerms(TermsAdapter terms){
        this.terms = terms;
    }

    public void selectTerm(final String term) {
        if(!this.term.equals(term))
            this.term = term;
    }

    @JavascriptInterface
    public void loadCourses(String body){
        TimeTable timeTable = new TimeTable();
        Document doc = Jsoup.parse(body);
        Elements tables = doc.select("table.datadisplaytable");
        for(int i=0; i<(tables.size()-1); i+=2){
            System.out.println("Table # " + (i/2));
            Course course = new Course("","");
            Element tableDetail = tables.get(i),
                    tableTimes = tables.get(i+1);

            String courseLongName = tableDetail.getElementsByTag("caption").get(0).text();
            int firstDash = courseLongName.indexOf("-");
            course.setTitle(courseLongName.substring(0, firstDash));
            //Only the course code and stream are left
            courseLongName = courseLongName.substring(firstDash+2);
            int secondDash = courseLongName.indexOf("-");
            course.setCourseCode(courseLongName.substring(0, secondDash));
            //Only the stream is now left
            courseLongName = courseLongName.substring(secondDash+2);
            course.setStream(courseLongName);

            Elements rows = tableTimes.getElementsByTag("td");
            String dateRange = rows.get(4).text();
            course.setDateRange(dateRange);
            try {
                course.setInstructorName(rows.get(6).ownText().substring(0, rows.get(6).ownText().length() - 3)); //Might need to be stripped
            } catch (StringIndexOutOfBoundsException e){
                //No instructor
                course.setInstructorName(rows.get(6).text().substring(0, rows.get(6).text().length())); //Might need to be stripped
            }


            rows = tableTimes.getElementsByTag("tr");
            boolean incorrectTable= false;
            for(int j=1; j<rows.size(); j++){
                try {
                    Elements tds = rows.get(j).getElementsByTag("td");
                    ComponentDate date = new ComponentDate();
                    date.setTime(tds.get(1).text());
                    date.setVenue(tds.get(3).text());
                    date.setDayOfWeek(tds.get(2).text());
                    String start = dateRange.substring(0, dateRange.indexOf("-")-1),
                            end = dateRange.substring(dateRange.indexOf("-")+2, dateRange.length());
                    List<Calendar> dates = setCalendarDates(start, end);
                    course.addDate(date);
                    course.setStart(dates.get(0));
                    course.setEnd(dates.get(1));
                } catch (IndexOutOfBoundsException e){
                    //Incorrect table
                    i++;
                    incorrectTable = true;
                    break;
                }
            }
            if(!incorrectTable) {
                timeTable.addCourse(course);
            }
        }
        student.setTimeTable(timeTable);
        student.addTimeTable(timeTable, term);

        if(timetableActivity != null) {
            timetableActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timetableActivity.updateCourses();
                    swipe1.setRefreshing(false);
                }
            });
        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((NavigationView)mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().getItem(0).setEnabled(true);
                ((NavigationView)mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().getItem(0).setActionView(null);
                ((NavigationView)mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().getItem(2).setEnabled(true);

                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        view.loadUrl("javascript:window.sasConfig.loadTranscript('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>');");
                        super.onPageFinished(view, url);
                    }
                });
                String formData = "levl=UG&tprt=WEB";
                webView.postUrl(resources.getString(R.string.transcript_post), formData.getBytes());
            }
        });
    }

    @JavascriptInterface
    public void loadTranscript(String body){

        Transcript transcript = new Transcript();
        Document document = Jsoup.parse(body);
        Elements tables = document.getElementsByTag("table");
        Element studentInformationTable = tables.get(5);
        Elements tds = studentInformationTable.getElementsByClass("dddefault");
        Elements ths = studentInformationTable.getElementsByClass("ddlabel");
        String degreeType = tds.get(1).ownText();
        String program = tds.get(2).ownText();
        String faculty = tds.get(3).ownText();
        String major = tds.get(5).ownText();
        String minor = tds.get(6).ownText();

        transcript.setDegree(degreeType)
                .setProgram(program)
                .setFaculty(faculty)
                .setMajor(major)
                .setMinor(minor);

        List<Term> terms = new ArrayList<>();

        int startTermIndex,
                startCourseIndex;
        if(minor.contains("Bachelor")) {
            startTermIndex = 13;
            startCourseIndex = 16;
        } else {
            startTermIndex = 15;
            startCourseIndex = 18;
        }
        while(true){
            Term term = new Term();
            String termName = ths.get(startTermIndex).text().substring(6);
            term.setName(termName);
            List<Term.Course> courses = new ArrayList<>();
             while(true){
                 Term.Course course = new Term.Course();
                 String subject, code, title, score, grade, creditHours;
                 try {
                     subject = tds.get(startCourseIndex).ownText();
                     code = tds.get(startCourseIndex+1).ownText();
                     title = tds.get(startCourseIndex + 4).text().substring(0, tds.get(startCourseIndex + 4).text().indexOf("Final"));
                     score = tds.get(startCourseIndex + 4).text().substring(tds.get(startCourseIndex + 4).text().indexOf("Final") + 6);
                     grade = tds.get(startCourseIndex+5).ownText();
                     creditHours = tds.get(startCourseIndex+6).text();
                 } catch (StringIndexOutOfBoundsException e){
                     startCourseIndex -= 1;
                     subject = tds.get(startCourseIndex).ownText();
                     code = tds.get(startCourseIndex+1).ownText();
                     title = tds.get(startCourseIndex + 4).text();
                     score = "In Progress";
                     grade = "";
                     creditHours = tds.get(startCourseIndex+5).text();
                 }

                 course.setSubject(subject)
                         .setCode(code)
                         .setTitle(title)
                         .setScore(score)
                         .setGrade(grade)
                         .setCreditHours(creditHours);

                 courses.add(course);
                 try {
                     if (tds.get(startCourseIndex + 8).text().contains(".00")) {
                         startCourseIndex += 18;
                         break;
                     } else {
                         startCourseIndex += 8;
                     }
                 } catch (IndexOutOfBoundsException e){
                     break;
                 }
             }
            term.setCourses(courses);
            terms.add(term);
            try {
                if (ths.get(startTermIndex + 7).text().contains("Term:")) {
                    startTermIndex += 7;
                } else {
                    break;
                }
            } catch(IndexOutOfBoundsException e){
                break;
            }
        }
        transcript.setTerms(terms);
        student.setTranscript(transcript);

        if(transcriptAdapter != null && transcriptActivity != null) {
            transcriptActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    transcriptAdapter.updateTerms(student.getTranscript().getTerms());
                    swipe2.setRefreshing(false);
                }
            });

        }

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((NavigationView) mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript).setActionView(null);
                ((NavigationView) mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().findItem(R.id.sas_transcript).setEnabled(true);
            }
        });
    }

    public static List<Calendar> setCalendarDates(String start, String end){
        Calendar startDate = Calendar.getInstance(),
                endDate = Calendar.getInstance();
        List<Calendar> calendars = new ArrayList<>();
        String startMonthString = start.substring(0,3),
        endMonthString = end.substring(0,3);
        int startDayOfMonth = Integer.valueOf(start.substring(4,start.indexOf(","))),
        endDayOfMonth = Integer.valueOf(end.substring(4,end.indexOf(","))),
        startYear = Integer.valueOf(start.substring(start.indexOf(",")+2,start.length())),
        endYear = Integer.valueOf(end.substring(end.indexOf(",")+2,end.length()));
        switch (startMonthString){
            case "Jan":
                startDate.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case "Feb":
                startDate.set(Calendar.MONTH, Calendar.FEBRUARY);
                break;
            case "Mar":
                startDate.set(Calendar.MONTH, Calendar.MARCH);
                break;
            case "Apr":
                startDate.set(Calendar.MONTH, Calendar.APRIL);
                break;
            case "May":
                startDate.set(Calendar.MONTH, Calendar.MAY);
                break;
            case "Jun":
                startDate.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case "Jul":
                startDate.set(Calendar.MONTH, Calendar.JULY);
                break;
            case "Aug":
                startDate.set(Calendar.MONTH, Calendar.AUGUST);
                break;
            case "Sep":
                startDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
                break;
            case "Oct":
                startDate.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case "Nov":
                startDate.set(Calendar.MONTH, Calendar.NOVEMBER);
                break;
            case "Dec":
                startDate.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }
        switch (endMonthString){
            case "Jan":
                endDate.set(Calendar.MONTH, Calendar.JANUARY);
                break;
            case "Feb":
                endDate.set(Calendar.MONTH, Calendar.FEBRUARY);
                break;
            case "Mar":
                endDate.set(Calendar.MONTH, Calendar.MARCH);
                break;
            case "Apr":
                endDate.set(Calendar.MONTH, Calendar.APRIL);
                break;
            case "May":
                endDate.set(Calendar.MONTH, Calendar.MAY);
                break;
            case "Jun":
                endDate.set(Calendar.MONTH, Calendar.JUNE);
                break;
            case "Jul":
                endDate.set(Calendar.MONTH, Calendar.JULY);
                break;
            case "Aug":
                endDate.set(Calendar.MONTH, Calendar.AUGUST);
                break;
            case "Sep":
                endDate.set(Calendar.MONTH, Calendar.SEPTEMBER);
                break;
            case "Oct":
                endDate.set(Calendar.MONTH, Calendar.OCTOBER);
                break;
            case "Nov":
                endDate.set(Calendar.MONTH, Calendar.NOVEMBER);
                break;
            case "Dec":
                endDate.set(Calendar.MONTH, Calendar.DECEMBER);
                break;
        }
        startDate.set(Calendar.DAY_OF_MONTH, startDayOfMonth);
        endDate.set(Calendar.DAY_OF_MONTH, endDayOfMonth);
        startDate.set(Calendar.YEAR, startYear);
        endDate.set(Calendar.YEAR, endYear);
        calendars.add(0, startDate);
        calendars.add(1, endDate);
        return calendars;
    }

    public void setmActivity(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setLogin(AccountAuthenticatorActivity login) {
        this.login = login;
    }

    public void setTimetableActivity(SASTimetableActivity timetableActivity) {
        this.timetableActivity = timetableActivity;
    }

    public void setSwipe1(SwipeRefreshLayout swipe1) {
        this.swipe1 = swipe1;
    }

    public void setTranscriptActivity(SASTranscriptActivity transcriptActivity) {
        this.transcriptActivity = transcriptActivity;
    }

    public void setTranscriptAdapter(SASTranscriptAdapter transcriptAdapter) {
        this.transcriptAdapter = transcriptAdapter;
    }

    public void setSwipe2(SwipeRefreshLayout swipe2) {
        this.swipe2 = swipe2;
    }
}
