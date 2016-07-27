package com.uwimonacs.fstmobile.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Course {
    private String courseCode, title, stream, instructorName, dateRange;
    private int numberOfWeeks;
    private List<ComponentDate> dates;
    private Calendar start, end;

    public Course(String code, String title){
        this.courseCode = code;
        this.title = title;
        dates = new ArrayList<>();
    }

    public void addDate(ComponentDate date){
        dates.add(date);
    }

    public List<ComponentDate> getDates() {
        return dates;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfWeeks() {
        return numberOfWeeks;
    }

    public void setNumberOfWeeks(int numberOfWeeks) {
        this.numberOfWeeks = numberOfWeeks;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }
}
