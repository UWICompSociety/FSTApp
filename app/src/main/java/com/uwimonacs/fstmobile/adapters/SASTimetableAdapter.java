package com.uwimonacs.fstmobile.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.ComponentDate;
import com.uwimonacs.fstmobile.models.Course;
import com.uwimonacs.fstmobile.models.TimetableViewHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SASTimetableAdapter extends RecyclerView.Adapter<TimetableViewHolder> {
    private List<Course> courses;
    private List<ComponentDate> dates;
    private List<String> times, venues, codeTitles;
    private String currentClass;
    private final Context context;

    public SASTimetableAdapter(List<Course> courses, List<ComponentDate> dates, Context context) {
        this.courses = courses;
        this.dates = dates;
        currentClass = "";
        this.context = context;
        orderByTime();
    }

    @Override
    public TimetableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TimetableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_sastimetable_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TimetableViewHolder holder, int position) {
        holder.codeTitle.setText(codeTitles.get(holder.getAdapterPosition()));
        holder.times.setText(times.get(holder.getAdapterPosition()));
        holder.venue.setText(venues.get(holder.getAdapterPosition()));
        if (currentClass.equals(times.get(holder.getAdapterPosition())))
            holder.classCard.setBackgroundColor(ContextCompat.getColor(context, R.color.cardview_dark_background));
    }

    @Override
    public int getItemCount() {
        return times.size();
    }

    public void updateCourses(List<Course> courses, Calendar newDate) {
        final int dayOfWeek = newDate.get(Calendar.DAY_OF_WEEK);
        System.out.println("Day of Week: " + dayOfWeek);
        this.courses.clear();
        this.dates.clear();
        for (int i = 0; i < courses.size(); i++) {
            final List<ComponentDate> dates = courses.get(i).getDates();
            for (int j = 0; j < dates.size(); j++){
                if (dates.get(j).getDay() == dayOfWeek) {
                    System.out.println("Course day: " + dates.get(j).getDay());
                    this.courses.add(courses.get(i));
                    this.dates.add(dates.get(j));
                    break;
                }
            }
        }
        orderByTime();
        notifyDataSetChanged();
    }

    private void orderByTime() {
        final List<String> codeTitles = new ArrayList<>();
        final List<String> times = new ArrayList<>();
        final List<String> venues = new ArrayList<>();
        final List<String> AMs = new ArrayList<>(),
                PMs = new ArrayList<>(),
                CT1 = new ArrayList<>(),
                CT2 = new ArrayList<>(),
                V1 = new ArrayList<>(),
                V2 = new ArrayList<>();
        final long currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int iCT = Integer.valueOf("" + currentTime);
        for (int i = 0; i < dates.size(); i++) {
//          Use a kind of merge sort to arrange am and pm separately
                String time = dates.get(i).getTime(),
                        codeTitle = courses.get(i).getCourseCode() +
                        " - " + courses.get(i).getTitle(),
                        venue = dates.get(i).getVenue();
                int suffixIndex = time.indexOf("am");
                if (suffixIndex == -1)
                    suffixIndex = time.indexOf("pm");
                String suffix = time.substring(suffixIndex, suffixIndex + 2);
                System.out.println("Time of day " + suffix);
                if (suffix.equals("am")){
                    suffixIndex = time.indexOf(":");
                    int secondSuffixIndex = time.indexOf(":", suffixIndex + 1);
                    suffix = time.substring(0, suffixIndex);
                    String secondSuffix = time.substring(secondSuffixIndex - 1, secondSuffixIndex);
                    System.out.println("Actual time " + suffix + "am");
                    int intSuffix = Integer.valueOf(suffix),
                    intSecondSuffix = Integer.valueOf(secondSuffix);
                    if (intSuffix != intSecondSuffix && iCT >=intSuffix && iCT <= intSecondSuffix) {
                        currentClass = time;
                    } else if(iCT == intSuffix) {
                        currentClass = time;
                    }
                    System.out.println("Actual time integer " + intSuffix + "am");
                    //Sort AMs
                    if (AMs.size() == 0) {
                        AMs.add(time);
                        CT1.add(codeTitle);
                        V1.add(venue);
                    } else {
                        int index = 0;
                        for (int j=0; j < AMs.size(); j++) {
                            String oldAm = AMs.get(j);
                            int intAm = Integer.valueOf(oldAm.substring(0, oldAm.indexOf(":")));
                            if (intSuffix > intAm) {
                                index = j+1;
                            } else {
                                break;
                            }
                        }
                        if (index < AMs.size()) {
                            AMs.add(index, time);
                            CT1.add(index, codeTitle);
                            V1.add(index, venue);
                        }
                        else {
                            AMs.add(time);
                            CT1.add(codeTitle);
                            V1.add(venue);
                        }
                    }
                } else {
                    iCT -= 12;
                    suffixIndex = time.indexOf(":");
                    int secondSuffixIndex = time.indexOf(":", suffixIndex + 1);
                    suffix = time.substring(0, suffixIndex);
                    String secondSuffix = time.substring(secondSuffixIndex - 1, secondSuffixIndex);
                    System.out.println("Actual time " + suffix + "pm");
                    int intSuffix = Integer.valueOf(suffix),
                    intSecondSuffix = Integer.valueOf(secondSuffix);
                    if (intSuffix != intSecondSuffix && iCT >=intSuffix && iCT <= intSecondSuffix) {
                        currentClass = time;
                    } else if (iCT == intSuffix) {
                        currentClass = time;
                    }
                    System.out.println("Actual time integer " + intSuffix + "pm");
                    //Sort PMs
                    if (PMs.size() == 0) {
                        PMs.add(time);
                        CT2.add(codeTitle);
                        V2.add(venue);
                    } else {
                        int index = 0;
                        for (int j = 0; j < PMs.size(); j++) {
                            final String oldPm = PMs.get(j);
                            int intPm = Integer.valueOf(oldPm.substring(0, oldPm.indexOf(":")));
                            if (intSuffix > intPm) {
                                index = j + 1;
                            } else {
                                break;
                            }
                        }
                        if (index < PMs.size()) {
                            PMs.add(index, time);
                            CT2.add(index, codeTitle);
                            V2.add(index, venue);
                        }
                        else {
                            PMs.add(time);
                            CT2.add(codeTitle);
                            V2.add(venue);
                        }
                    }
                }
        }
        for (int i = 0; i < AMs.size(); i++) {
            codeTitles.add(AMs.get(i));
        }
        for (int i = 0; i < PMs.size(); i++) {
            codeTitles.add(PMs.get(i));
        }
        for (int i = 0; i < CT1.size(); i++) {
            times.add(CT1.get(i));
        }
        for (int i = 0; i < CT2.size(); i++) {
            times.add(CT2.get(i));
        }
        for (int i = 0; i < V1.size(); i++) {
            venues.add(V1.get(i));
        }
        for (int i = 0; i < V2.size(); i++) {
            venues.add(V2.get(i));
        }
        this.codeTitles = codeTitles;
        this.times = times;
        this.venues = venues;
    }
}
