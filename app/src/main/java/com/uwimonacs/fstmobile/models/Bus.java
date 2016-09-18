package com.uwimonacs.fstmobile.models;

import android.content.Context;

import com.uwimonacs.fstmobile.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author sultanofcardio
 */
public class Bus {
    private Route route;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public static class Route{
        private ArrayList<Trip> trips;
        private String title, name;
        private int currentTrip;

        /*
        * What information should I be able to get from this model?
        *
        * Where is bus X right now? done
        * Where will bus X be at M time?
        * What is the complete schedule for bus X?
        * Notify me when bus X is at stop N
        * */

        public Route(String title, String name){
            this.title = title;
            this.name = name;
            this.trips = new ArrayList<>();
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<Trip> getTrips() {
            return trips;
        }

        public void setTrips(ArrayList<Trip> trips) {
            this.trips = trips;
        }

        private void determineCurrentTrip(){
            Trip trip = trips.get(0);
            List<Stop> stops = trip.getStops();

            int startHour = stops.get(0).getTime().get(Calendar.HOUR_OF_DAY);
            int startMinute = stops.get(0).getTime().get(Calendar.MINUTE);
            int duration = trips.get(0).getDuration();

            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);

            if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                currentTrip = -1;
            }
            else if(hour < startHour){
                currentTrip = -1;
            }
            else if(duration == 1 && startMinute < 30) {
                currentTrip = (hour - startHour);
            } else if(duration == 1 && startMinute == 30) {
                if(minute < 30)
                    currentTrip = (hour - startHour) - 1;
                else
                    currentTrip = (hour - startHour);
            } else {
                if(minute < 30){
                    currentTrip = (2*(hour - startHour)) - 1;
                } else {
                    currentTrip = 2*(hour - startHour);
                }
            }
        }

        public Trip getCurrentTrip(){
            determineCurrentTrip();
            if(currentTrip < 0 || currentTrip > trips.size())
                throw new IndexOutOfBoundsException();
            return trips.get(currentTrip);
        }

    }

    public static class Trip{
        private int number;
        /*
        * 1 - 1 hour
        * 2 - 1/2 hour
        * */
        private int duration;
        private List<Stop> stops;
        private Context context;

        public Trip(int number, int duration, Context context){
            this.number = number;
            this.duration = duration;
            this.context = context;
            stops = new ArrayList<>();
        }

        public int getNumber() {
            return number;
        }

        public int getDuration() {
            return duration;
        }

        public List<Stop> getStops() {
            return stops;
        }

        public void initStops(String routeTitle){
            stops = new ArrayList<>();
            int busStops = Constants.resolveRoute(routeTitle);
            int busTimes = Constants.resolveTime(routeTitle);
            String[] stopArray = context.getResources().getStringArray(busStops);
            String[] timeArray = context.getResources().getStringArray(busTimes);
            for(int i=0; i<timeArray.length; i++){
                String time = timeArray[i];
                int hour = 0, minute = 0;
                if(time.length() == 4) {
                    hour = Integer.valueOf(timeArray[i].substring(0, 1));
                    minute = Integer.valueOf(timeArray[i].substring(2));
                    if(duration == 1)
                        hour += number;
                    else if(duration == 2)
                        minute += (number * 30);
                }
                else if(time.length() == 5){
                    hour = Integer.valueOf(timeArray[i].substring(0, 2));
                    minute = Integer.valueOf(timeArray[i].substring(3));
                    if(duration == 1)
                        hour += number;
                    else if(duration == 2)
                        minute += (number * 30);
                }

                Stop stop = new Stop();
                stop.setName(stopArray[i]);
                stop.setTime(hour, minute);
                stops.add(stop);
            }
        }

        public String whereIsBus(){
            /*
            * Strategy
            *
            * Get current hour.
            * Determine which trip the bus is on
            * Determine which stop(if any) the bus is on and return stop
            * If not, determine which two stops the bus is between and return the second stop
            * */

            String stopName = "";

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);

            System.out.println("Time now = " + hour + ":" + minute);

            int stopHour;
            int stopMinute;

            for(Stop stop: stops){
                stopHour = stop.getTime().get(Calendar.HOUR_OF_DAY);
                stopMinute = stop.getTime().get(Calendar.MINUTE);
                System.out.println("Whereisbustime = " + stopHour + ":" + stopMinute);
                if(hour == stopHour){
                    if(minute == stopMinute) {
                        stopName = "at " + stop.getName();
                        break;
                    } else if(minute < stopMinute) {
                        stopName = "on the way to " + stop.getName();
                        break;
                    }
                }
            }
            return stopName;
        }
    }

    public static class Stop{
        private String name;
        private Calendar time;

        public Stop(){
            time = Calendar.getInstance();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Calendar getTime() {
            return time;
        }

        /**
         * Set the exact time of this stop
         * @param hour The hour of the day using the 24-Hour clock
         * @param minute The minute of the hour
         */
        public void setTime(int hour, int minute) {
            this.time.set(Calendar.HOUR_OF_DAY, hour);
            this.time.set(Calendar.MINUTE, minute);
        }
    }
}
