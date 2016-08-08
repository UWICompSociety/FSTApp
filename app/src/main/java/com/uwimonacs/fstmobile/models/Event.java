package com.uwimonacs.fstmobile.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Matthew on 8/8/2016.
 */

@Table(name="Event")
public class Event extends Model {


    @SerializedName("id")
    @Column(name="eventId")
    int eventId;


    @SerializedName("title")
    @Column(name="title")
    String title;

    @SerializedName("date")
    @Column(name="date")
    String date;

    @SerializedName("description")
    @Column(name="description")
    String description;


    public Event()
    {
        super();
    }

    public Event(String title,String date,String description)
    {
        super();
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public static Event findOrCreateFromJson(Event new_event) {
        int eventId = new_event.getEventId();
        Event existingEvent =
                new Select().from(Event.class).where("eventId = ?", eventId).executeSingle();
        if (existingEvent != null) {
            // found and return existing
            UpdateEvent(existingEvent,new_event);
            return existingEvent;
        } else {
            // create and return new user
            Event event = new_event;
            event.save();
            return event;
        }
    }

    private static void UpdateEvent(Event old_event,Event new_event)
    {
        old_event.setTitle(new_event.getTitle());
        old_event.setDescription(new_event.getDescription());
        old_event.setDate(new_event.getDate());
        old_event.save();

    }
}
