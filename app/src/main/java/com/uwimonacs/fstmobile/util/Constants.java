package com.uwimonacs.fstmobile.util;

import com.uwimonacs.fstmobile.R;

public class Constants {


    public static final String HOST_URL =  "http://fast.mona.uwi.edu";


    public static final String CONTACTS_URL = HOST_URL + "/contacts/";

    public static final String NEWS_URL  = HOST_URL + "/news/";

    public static final String FAQS_URL = HOST_URL + "/faqs/";

    public static final String SCHOL_URL = HOST_URL + "/scholarship/";

    public static final String PLACE_URL = HOST_URL + "/places/";


    public static final String IMAGES_URL = HOST_URL + "/images/";

    public static final String EVENTS_URL = HOST_URL + "/events/";


    //TODO: Update alerts URL
    public static final String ALERTS_URL = HOST_URL + "";


    private static final int BUS_ROUTE_A_STOPS = R.array.bus_route_a_stops;
    private static final int BUS_ROUTE_B_STOPS = R.array.bus_route_b_stops;
    private static final int BUS_ROUTE_C_STOPS = R.array.bus_route_c_stops;
    private static final int BUS_ROUTE_D_STOPS = R.array.bus_route_d_stops;

    private static final int BUS_ROUTE_A_TIMES = R.array.bus_route_a_times;
    private static final int BUS_ROUTE_B_TIMES = R.array.bus_route_b_times;
    private static final int BUS_ROUTE_C_TIMES = R.array.bus_route_c_times;
    private static final int BUS_ROUTE_D_TIMES = R.array.bus_route_d_times;

    public static int resolveRoute(String route){
        switch(route){
            case "A":
                return BUS_ROUTE_A_STOPS;
            case "B":
                return BUS_ROUTE_B_STOPS;
            case "C":
                return BUS_ROUTE_C_STOPS;
            case "D":
                return BUS_ROUTE_D_STOPS;
            default:
                return -1;
        }
    }

    public static int resolveTime(String route){
        switch(route){
            case "A":
                return BUS_ROUTE_A_TIMES;
            case "B":
                return BUS_ROUTE_B_TIMES;
            case "C":
                return BUS_ROUTE_C_TIMES;
            case "D":
                return BUS_ROUTE_D_TIMES;
            default:
                return -1;
        }
    }


}
