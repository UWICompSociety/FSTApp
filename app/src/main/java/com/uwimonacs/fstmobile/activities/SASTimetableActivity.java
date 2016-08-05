package com.uwimonacs.fstmobile.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.SASTimetableAdapter;
import com.uwimonacs.fstmobile.models.ComponentDate;
import com.uwimonacs.fstmobile.models.Course;
import com.uwimonacs.fstmobile.models.SASConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

@SuppressWarnings("FieldCanBeLocal")
public class SASTimetableActivity extends AppCompatActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    private SASConfig sasConfig;
    private SwipeRefreshLayout swipeRefreshLayout;
    private WebView webView;
    private AccountManager manager;
    private Account account;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sasConfig = MyApplication.getSasConfig();
        manager = AccountManager.get(this);
        account = manager.getAccountsByType("UWI")[0];
        webView = MyApplication.getWebView();

        setContentView(R.layout.activity_sastimetable);
        if(isFirstTime()){
            //Show tutorial
            new MaterialShowcaseView.Builder(this)
                    .setTarget(findViewById(R.id.circle_target))
                    .setDismissText("GOT IT")
                    .setContentText("Swipe down to refresh your timetable")
                    .show();
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.timetable_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        updateCourses();
        final MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.sas_timetable);
        calendarView.setCurrentDate(Calendar.getInstance());
        calendarView.setDateSelected(Calendar.getInstance(), true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sas_timetable_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final Calendar calendar = Calendar.getInstance();
        final List<ComponentDate> dates = new ArrayList<>();
        final List<Course> localCourses = new ArrayList<>();

        for (int i = 0; i < courses.size(); i++){
            if (compareDates(courses.get(i).getStart(), courses.get(i).getEnd(), calendar)) {
                final List<ComponentDate> courseDates = courses.get(i).getDates();
                for (int j = 0; j < courseDates.size(); j++) {
                    if (courseDates.get(j).getDay() == calendar.get(Calendar.DAY_OF_WEEK)) {
                        localCourses.add(courses.get(i));
                        dates.add(courseDates.get(j));
                        break;
                    }
                }
            }
        }
        final SASTimetableAdapter adapter = new SASTimetableAdapter(localCourses, dates, this);
        sasConfig.setTimetableActivity(this);
        sasConfig.setSwipe1(swipeRefreshLayout);
        recyclerView.setAdapter(adapter);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Calendar newDate = date.getCalendar();
                final List<Course> localCourses = new ArrayList<>();
                for (int i = 0; i < courses.size(); i++) {
                    final Calendar startDate = courses.get(i).getStart(),
                            endDate = courses.get(i).getEnd();
                    if (compareDates(startDate, endDate, newDate)) {
                        localCourses.add(courses.get(i));
                    }
                }
                adapter.updateCourses(localCourses, newDate);
            }
        });
    }

    public void updateCourses(){
        this.courses = sasConfig.student.getTimeTable().getCourses();
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("TimetableActivity", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("TimetableActivity", true);
            editor.apply();
        }
        return !ranBefore;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();

        return true;
    }

    public boolean compareDates(Calendar min, Calendar max, Calendar calendar) {
        boolean between = false;

        if (calendar.get(Calendar.YEAR) >= min.get(Calendar.YEAR) && calendar.get(Calendar.YEAR) <= max.get(Calendar.YEAR)) {
            if (min.get(Calendar.YEAR) == max.get(Calendar.YEAR)){
                if (calendar.get(Calendar.MONTH) >= min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= max.get(Calendar.MONTH)) {
                    //Compare days
                    if (calendar.get(Calendar.MONTH) == min.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) >= min.get(Calendar.DAY_OF_MONTH)) {
                        between = true;
                    }
                    else if (calendar.get(Calendar.MONTH) == max.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) <= max.get(Calendar.DAY_OF_MONTH)) {
                        between = true;
                    }
                    else if (calendar.get(Calendar.MONTH) > min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) < max.get(Calendar.MONTH)) {
                        between = true;
                    }
                }
            }
            else {
                max.set(Calendar.MONTH, (11 + min.get(Calendar.MONTH)));
                if (calendar.get(Calendar.MONTH) >= min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= max.get(Calendar.MONTH)) {
                    //Compare days
                    if (calendar.get(Calendar.MONTH) == min.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) >= min.get(Calendar.DAY_OF_MONTH)) {
                        between = true;
                    }
                    else if (calendar.get(Calendar.MONTH) == max.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) <= max.get(Calendar.DAY_OF_MONTH)) {
                        between = true;
                    }
                    else if (calendar.get(Calendar.MONTH) > min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) < max.get(Calendar.MONTH)) {
                        between = true;
                    }
                }
            }
        }
        return between;
    }

    @Override
    public void onRefresh() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.sasConfig.passiveLogin('<body>'+document.getElementsByTagName('body')[0].innerHTML+'</body>', 'timetable');");
                super.onPageFinished(view, url);
            }
        });
        String idNumber = sasConfig.student.getIdNumber();
        String password = manager.getPassword(account);
        String formData = "sid="+idNumber+"&PIN="+password;
        webView.postUrl(getResources().getString(R.string.login_post), formData.getBytes());
    }
}
