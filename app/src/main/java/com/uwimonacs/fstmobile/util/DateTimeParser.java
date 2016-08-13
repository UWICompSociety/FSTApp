package com.uwimonacs.fstmobile.util;

import java.util.Calendar;

/**
 * Created by Matthew on 11/30/2015.
 */
public class DateTimeParser {


    public static String parseDateTime(String datetime){

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int now_year = cal.get(Calendar.YEAR);

        String date = "";


        int date_index = datetime.indexOf("T");

        String date_sub_text = datetime.substring(0,date_index);

        String date_string[] = date_sub_text.split("-");

        String year = date_string[0];
        String month = date_string[1];
        String day = date_string[2];

        String time_sub_text = datetime.substring(date_index+1,datetime.length());

        String time_string[] = time_sub_text.split(":");

        String hrs = time_string[0];
        String mins = time_string[1];



        date = getMonth(month) + " " +day+ " " + " "+ year+ " " + hrs+":"+mins;









        return date;
    }

    private static String getMonth(String month_num){

        switch(month_num){
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return  "Dec";
            default:
                return "";
        }
    }
}
