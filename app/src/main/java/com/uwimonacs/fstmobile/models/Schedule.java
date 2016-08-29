package com.uwimonacs.fstmobile.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthew on 8/28/2016.
 */
public class Schedule {

    private String title;
    private String description;
    private String time;
    private String venue;


    public Schedule()
    {

    }

    public Schedule(String time,String title,String description)
    {
        this.time = time;
        this.title = title;
        this.description =description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static List<Schedule> getSchedule(String name)
    {
        List<Schedule> schedules = new ArrayList<>();
        char lstchar = name.toUpperCase().charAt(0);
        String session1 = "Registration Reminders";
        String session2 = "Get Connected";
        String session3 = "Balancing Act";

        String venue1 = "SLT1";
        String venue2 = "SLT2";
        String venue3 = "SLT3";
        String venue4 = "FST Spine";

        Schedule schedule0 = new Schedule();
        schedule0.setTitle("Get Set");
        schedule0.setTime("9:30-10:00");

        if ((lstchar>='A' && lstchar<='D') || (lstchar>='N' && lstchar<='P'))
        {
            schedule0.setVenue(venue2);
            schedules.add(schedule0);


            Schedule schedule1 = new Schedule();
            schedule1.setTitle("Session 1:"+session1 );
            schedule1.setTime("10:00-10:30");
            schedule1.setVenue(venue2);

            schedules.add(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setTitle("S-T-R-E-T-C-H");
            schedule2.setTime("10:30-10:50");
            schedule2.setVenue(venue4);

            schedules.add(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setTitle("Session 2:"+session2 );
            schedule3.setTime("10:50-11:20");
            schedule3.setVenue(venue3);

            schedules.add(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setTitle("S-T-R-E-T-C-H");
            schedule4.setTime("11:20-11:40");
            schedule4.setVenue(venue4);

            schedules.add(schedule4);

            Schedule schedule5 = new Schedule();
            schedule5.setTitle("Session 3:"+session3 );
            schedule5.setTime("11:40-12:10");
            schedule5.setVenue(venue1);

            schedules.add(schedule5);

            Schedule schedule6 = new Schedule();
            schedule6.setTitle("Session 3:"+"Go!");
            schedule6.setTime("12:10-12:30");
            schedule6.setVenue(venue1);

            schedules.add(schedule6);

            Schedule schedule7 = new Schedule();
            schedule7.setTitle("Academic Advising");
            schedule7.setTime("1:00-5:00");
            schedule7.setVenue("C6 & C7");

            schedules.add(schedule7);

        }

        if((lstchar>='E' && lstchar<='H') ||(lstchar>='Q' && lstchar<='T'))
        {
            schedule0.setVenue(venue3);
            schedules.add(schedule0);


            Schedule schedule1 = new Schedule();
            schedule1.setTitle("Session 1:"+session2 );
            schedule1.setTime("10:00-10:30");
            schedule1.setVenue(venue3);

            schedules.add(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setTitle("S-T-R-E-T-C-H");
            schedule2.setTime("10:30-10:50");
            schedule2.setVenue(venue4);

            schedules.add(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setTitle("Session 2:"+session3 );
            schedule3.setTime("10:50-11:20");
            schedule3.setVenue(venue1);

            schedules.add(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setTitle("S-T-R-E-T-C-H");
            schedule4.setTime("11:20-11:40");
            schedule4.setVenue(venue4);

            schedules.add(schedule4);

            Schedule schedule5 = new Schedule();
            schedule5.setTitle("Session 3:"+session1 );
            schedule5.setTime("11:40-12:10");
            schedule5.setVenue(venue2);

            schedules.add(schedule5);

            Schedule schedule6 = new Schedule();
            schedule6.setTitle("Session 3:"+"Go!");
            schedule6.setTime("12:10-12:30");
            schedule6.setVenue(venue2);

            schedules.add(schedule6);

            Schedule schedule7 = new Schedule();
            schedule7.setTitle("Academic Advising");
            schedule7.setTime("1:00-5:00");
            schedule7.setVenue("C6 & C7");

            schedules.add(schedule7);
        }

        if((lstchar>='I' && lstchar<='M') ||(lstchar>='U' && lstchar<='Z'))
        {
            schedule0.setVenue(venue1);
            schedules.add(schedule0);


            Schedule schedule1 = new Schedule();
            schedule1.setTitle("Session 1:"+session3 );
            schedule1.setTime("10:00-10:30");
            schedule1.setVenue(venue1);

            schedules.add(schedule1);

            Schedule schedule2 = new Schedule();
            schedule2.setTitle("S-T-R-E-T-C-H");
            schedule2.setTime("10:30-10:50");
            schedule2.setVenue(venue4);

            schedules.add(schedule2);

            Schedule schedule3 = new Schedule();
            schedule3.setTitle("Session 2:"+session1 );
            schedule3.setTime("10:50-11:20");
            schedule3.setVenue(venue2);

            schedules.add(schedule3);

            Schedule schedule4 = new Schedule();
            schedule4.setTitle("S-T-R-E-T-C-H");
            schedule4.setTime("11:20-11:40");
            schedule4.setVenue(venue4);

            schedules.add(schedule4);

            Schedule schedule5 = new Schedule();
            schedule5.setTitle("Session 3:"+session2 );
            schedule5.setTime("11:40-12:10");
            schedule5.setVenue(venue3);

            schedules.add(schedule5);

            Schedule schedule6 = new Schedule();
            schedule6.setTitle("Session 3:"+"Go!");
            schedule6.setTime("12:10-12:30");
            schedule6.setVenue(venue3);

            schedules.add(schedule6);

            Schedule schedule7 = new Schedule();
            schedule7.setTitle("Academic Advising");
            schedule7.setTime("1:00-5:00");
            schedule7.setVenue("C6 & C7");

            schedules.add(schedule7);
        }



        return schedules;
    }
}
