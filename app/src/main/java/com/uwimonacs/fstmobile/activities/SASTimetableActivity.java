package com.uwimonacs.fstmobile.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.uwimonacs.fstmobile.MyApplication;
import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.adapters.SASTimetableAdapter;
import com.uwimonacs.fstmobile.models.ComponentDate;
import com.uwimonacs.fstmobile.models.Course;
import com.uwimonacs.fstmobile.models.SASConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SASTimetableActivity extends AppCompatActivity {
    private SASConfig sasConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sasConfig = MyApplication.getSasConfig();
        final List<Course> courses = sasConfig.student.getTimeTable().getCourses();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sastimetable);
        CalendarView calendarView = (CalendarView) findViewById(R.id.sas_timetable);
        assert calendarView != null;
        calendarView.setDate(System.currentTimeMillis());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sas_timetable_recyclerview);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Calendar calendar = Calendar.getInstance();
        List<ComponentDate> dates = new ArrayList<>();
        List<Course> localCourses = new ArrayList<>();
        for(int i=0; i<courses.size(); i++){
            if(compareDates(courses.get(i).getStart(), courses.get(i).getEnd(), calendar)) {
                List<ComponentDate> courseDates = courses.get(i).getDates();
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
        recyclerView.setAdapter(adapter);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(Calendar.YEAR, year);
                newDate.set(Calendar.MONTH, month);
                newDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                List<Course> localCourses = new ArrayList<>();
                for(int i=0; i<courses.size(); i++){
                    Calendar startDate = courses.get(i).getStart(),
                    endDate = courses.get(i).getEnd();
                    if(compareDates(startDate, endDate, newDate)){
                        localCourses.add(courses.get(i));
                    }
                }
                adapter.updateCourses(localCourses, newDate);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    public boolean compareDates(Calendar min, Calendar max, Calendar calendar){
        boolean between = false;
        if(calendar.get(Calendar.YEAR) >= min.get(Calendar.YEAR) && calendar.get(Calendar.YEAR) <= max.get(Calendar.YEAR)){
            if(min.get(Calendar.YEAR) == max.get(Calendar.YEAR)){
                if(calendar.get(Calendar.MONTH) >= min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= max.get(Calendar.MONTH)){
                    //Compare days
                    if(calendar.get(Calendar.MONTH) == min.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) >= min.get(Calendar.DAY_OF_MONTH)){
                        between = true;
                    }
                    else if(calendar.get(Calendar.MONTH) == max.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) <= max.get(Calendar.DAY_OF_MONTH)){
                        between = true;
                    }
                    else if(calendar.get(Calendar.MONTH) > min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) < max.get(Calendar.MONTH)){
                        between = true;
                    }
                }
            }
            else{
                max.set(Calendar.MONTH, (11 + min.get(Calendar.MONTH)));
                if(calendar.get(Calendar.MONTH) >= min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) <= max.get(Calendar.MONTH)){
                    //Compare days
                    if(calendar.get(Calendar.MONTH) == min.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) >= min.get(Calendar.DAY_OF_MONTH)){
                        between = true;
                    }
                    else if(calendar.get(Calendar.MONTH) == max.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) <= max.get(Calendar.DAY_OF_MONTH)){
                        between = true;
                    }
                    else if(calendar.get(Calendar.MONTH) > min.get(Calendar.MONTH) && calendar.get(Calendar.MONTH) < max.get(Calendar.MONTH)){
                        between = true;
                    }
                }
            }
        }
        return between;
    }
}
