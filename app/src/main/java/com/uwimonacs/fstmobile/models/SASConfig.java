package com.uwimonacs.fstmobile.models;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.activities.MainActivity;
import com.uwimonacs.fstmobile.activities.SASLoginActivity;
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
public class SASConfig {
    public Student student;
    public Context context;
    public String term = "";
    public int semester;
    private Resources resources;
    public List<String> termValues, termNames;
    private WebView webView;
    private AppCompatActivity mActivity;
    private AccountAuthenticatorActivity login;
    private AccountManager mAccountManager;
    private TermsAdapter terms;

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
        semester = 1;
    }

    /**
     * Sends HTTP POST Request with login information to
     * SAS Website to initiate a session
     */
    @JavascriptInterface
    public void Login(String body, String activity){
        if(body.contains("Authorization Failure") || body.contains("Invalid login information")) {
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            login.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    login.findViewById(R.id.sas_login_progressbar).setVisibility(View.GONE);
                    login.findViewById(R.id.sas_login_mainlayout).setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            Document document = Jsoup.parse(body);
            Element p = document.getElementsByClass("text-intro").get(0);
            String name = p.text().substring(5, p.text().indexOf("Student ID#:"));
            student.setName(name);

            if(activity.equals("main")){
                MainActivity.loggedIn = true;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView username = (TextView) mActivity.findViewById(R.id.username),
                        idNUmber = (TextView) mActivity.findViewById(R.id.id_number);
                        assert username != null;
                        username.setText(student.getName());
                        assert idNUmber != null;
                        idNUmber.setText(student.getIdNumber());

                        if(termNames.size() == 0) {
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
            } else {
                MainActivity.loggedIn = true;
                Toast.makeText(context, "Account added", Toast.LENGTH_SHORT).show();
                String accountType = "UWI";
                String username = student.getIdNumber();
                String password = student.getPassword();
                Account sasAccount = new Account(username, accountType);
                mAccountManager = AccountManager.get(login);
                mAccountManager.addAccountExplicitly(sasAccount, password, null);

                final Intent intent = new Intent();
                intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
                intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                intent.putExtra(AccountManager.KEY_AUTHTOKEN, password);
                login.setAccountAuthenticatorResult(intent.getExtras());
                login.setResult(SASLoginActivity.RESULT_OK, intent);
                login.finish();
            }
        }
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

    public void selectTerm(final String term) {
        if(!this.term.equals(term))
            this.term = term;
    }

    @JavascriptInterface
    public void loadCourses(String body){
        TimeTable timeTable = new TimeTable();
        timeTable.setSemester(semester);
        Document doc = Jsoup.parse(body);
        Elements tables = doc.select("table.datadisplaytable");
        for(int i=0; i<(tables.size()-1); i+=2){
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
            course.setInstructorName(rows.get(6).ownText().substring(0, rows.get(6).ownText().length()-3)); //Might need to be stripped


            rows = tableTimes.getElementsByTag("tr");
            for(int j=1; j<rows.size(); j++){
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
            }
            timeTable.addCourse(course);
        }
        student.setTimeTable(timeTable);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((NavigationView)mActivity.findViewById(R.id.nav_drawer)).getMenu().getItem(0).getSubMenu().getItem(0).setEnabled(true);
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
        String name = tds.get(0).ownText();
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

        System.out.println("Minor: " + minor);
        System.out.println("Start term index: " + startTermIndex);
        while(true){
            Term term = new Term();
            String termName = ths.get(startTermIndex).text().substring(6);
            System.out.println("Term: " + termName);
            term.setName(termName);
            List<Term.Course> courses = new ArrayList<>();
             while(true){
                 Term.Course course = new Term.Course();
                 String subject, code, title, score, grade, creditHours;
                 try {
                     subject = tds.get(startCourseIndex).ownText();
                     System.out.println("Subject: " + subject);
                     code = tds.get(startCourseIndex+1).ownText();
                     title = tds.get(startCourseIndex + 4).text().substring(0, tds.get(startCourseIndex + 4).text().indexOf("Final"));
                     score = tds.get(startCourseIndex + 4).text().substring(tds.get(startCourseIndex + 4).text().indexOf("Final") + 6);
                     grade = tds.get(startCourseIndex+5).ownText();
                     creditHours = tds.get(startCourseIndex+6).text();
                 } catch (StringIndexOutOfBoundsException e){
                     startCourseIndex -= 1;
                     subject = tds.get(startCourseIndex).ownText();
                     System.out.println("Subject: " + subject);
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
                         System.out.println("Error recovery: " + tds.get(startCourseIndex + 8).text());
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
        System.out.println("No. of terms: " +  terms.size());
        transcript.setTerms(terms);
        student.setTranscript(transcript);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
}
