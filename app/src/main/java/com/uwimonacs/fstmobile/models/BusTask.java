package com.uwimonacs.fstmobile.models;

import android.content.Context;
import android.os.AsyncTask;

import com.uwimonacs.fstmobile.R;

import java.util.ArrayList;

/**
 * @author sultanofcardio
 */
public class BusTask extends AsyncTask<Void, Void, ArrayList<Bus>> {
    private AsyncResponse response;
    private Context context;

    public BusTask(AsyncResponse response, Context context){
        this.response = response;
        this.context = context;
    }

    @Override
    protected ArrayList<Bus> doInBackground(Void... voids) {
        ArrayList<Bus> buses = new ArrayList<>();

        Bus bus1 = new Bus();
        Bus.Route route1 = new Bus.Route(context.getString(R.string.bus_route_a_title), context.getString(R.string.bus_route_a));
        ArrayList<Bus.Trip> trips1 = new ArrayList<>();
        for(int i=0; i < context.getResources().getInteger(R.integer.bus_route_a_trips); i++){
            Bus.Trip trip = new Bus.Trip(i, 1, context);
            trip.initStops(route1.getTitle());
            trips1.add(trip);
        }
        route1.setTrips(trips1);
        bus1.setRoute(route1);
        buses.add(bus1);


        Bus bus2 = new Bus();
        Bus.Route route2 = new Bus.Route(context.getString(R.string.bus_route_b_title), context.getString(R.string.bus_route_b));
        ArrayList<Bus.Trip> trips2 = new ArrayList<>();
        for(int i=0; i < context.getResources().getInteger(R.integer.bus_route_b_trips); i++){
            Bus.Trip trip = new Bus.Trip(i, 1, context);
            trip.initStops(route2.getTitle());
            trips2.add(trip);
        }
        route2.setTrips(trips2);
        bus2.setRoute(route2);
        buses.add(bus2);

        Bus bus3 = new Bus();
        Bus.Route route3 = new Bus.Route(context.getString(R.string.bus_route_c_title), context.getString(R.string.bus_route_c));
        ArrayList<Bus.Trip> trips3 = new ArrayList<>();
        for(int i=0; i < context.getResources().getInteger(R.integer.bus_route_c_trips); i++){
            Bus.Trip trip = new Bus.Trip(i, 2, context);
            trip.initStops(route3.getTitle());
            trips3.add(trip);
        }
        route3.setTrips(trips3);
        bus3.setRoute(route3);
        buses.add(bus3);

        Bus bus4 = new Bus();
        Bus.Route route4 = new Bus.Route(context.getString(R.string.bus_route_d_title), context.getString(R.string.bus_route_d));
        ArrayList<Bus.Trip> trips4 = new ArrayList<>();
        for(int i=0; i < context.getResources().getInteger(R.integer.bus_route_d_trips); i++){
            Bus.Trip trip = new Bus.Trip(i, 2, context);
            trip.initStops(route4.getTitle());
            trips4.add(trip);
        }
        route4.setTrips(trips4);
        bus4.setRoute(route4);
        buses.add(bus4);

        return buses;
    }

    @Override
    protected void onPostExecute(ArrayList<Bus> buses) {
        super.onPostExecute(buses);

        response.processFinish(buses);
    }
}
