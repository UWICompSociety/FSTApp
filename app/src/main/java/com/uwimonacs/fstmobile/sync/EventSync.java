package com.uwimonacs.fstmobile.sync;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.models.ImageItem;
import com.uwimonacs.fstmobile.rest.RestEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 8/8/2016.
 */
public class EventSync {

    private final String url;
    private ArrayList<Event> events = new ArrayList<>();

    public EventSync(String url)
    {
        this.url = url;
    }

    public boolean syncEvents() {

        final RestEvent restEvent = new RestEvent(url);

        events = restEvent.getEvents(); // gets the list of events from rest api

        if (events == null) // if there are no events
            return false;

        if (events.size() == 0) // if the events list is empty
            return false;

        ActiveAndroid.beginTransaction();
        try {
            deleteStaleData();
            for (int i = 0; i < events.size(); i++) {
                final Event event = events.get(i);

                Event.findOrCreateFromJson(event); // saves event to database
            }
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }

        return true;
    }

    private void deleteStaleData()
    {

        List<Event> stale_events = new Select().all().from(Event.class).execute();
        for(int i=0;i<stale_events.size();i++)
        {
            if(!doesEventExistInJson(stale_events.get(i)))
            {
                Event.delete(Event.class,stale_events.get(i).getId());
            }
        }
    }

    private boolean doesEventExistInJson(Event event)
    {
        return events.contains(event);
    }
}
