package com.uwimonacs.fstmobile.models;

import java.util.Calendar;

public class ComponentDate {
    private String time, venue, dayOfWeek;
    private int day;
    private int numberOfHours;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public int getDay() {
        switch(dayOfWeek){
            case "M":
                day = Calendar.MONDAY;
                break;
            case "T":
                day = Calendar.TUESDAY;
                break;
            case "W":
                day = Calendar.WEDNESDAY;
                break;
            case "R":
                day = Calendar.THURSDAY;
                break;
            case "F":
                day = Calendar.FRIDAY;
                break;
            case "S":
                day = Calendar.SATURDAY;
                break;
        }
        return day;
    }

    public void orderByTime(){

    }
}
