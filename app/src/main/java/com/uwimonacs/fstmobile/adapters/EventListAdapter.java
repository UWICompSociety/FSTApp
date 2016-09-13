package com.uwimonacs.fstmobile.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uwimonacs.fstmobile.R;
import com.uwimonacs.fstmobile.models.Event;
import com.uwimonacs.fstmobile.util.DateTimeParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Matthew on 8/9/2016.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {



    public final static int CALENDAR_PERMISSION = 0;
    public class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventNameText;
        TextView eventDescText;
        TextView eventDateText;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventNameText = (TextView) itemView.findViewById(R.id.eventTitle);
            eventDescText = (TextView) itemView.findViewById(R.id.eventDesc);
            eventDateText = (TextView) itemView.findViewById(R.id.eventDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    pos = EventViewHolder.this.getAdapterPosition();
                    final Event event = events.get(pos);
                    //show dialog
                    if (ContextCompat.checkSelfPermission(view.getContext(),
                            Manifest.permission.READ_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(view.getContext(),
                            Manifest.permission.WRITE_CALENDAR)
                            != PackageManager.PERMISSION_GRANTED ) {

                        ActivityCompat.requestPermissions((Activity)ctxt,
                               new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},CALENDAR_PERMISSION);

                    } else {
                            showDialog();
                    }


                }
            });
        }
    }


    Context ctxt;
    List<Event> events;
    private int pos;

    public EventListAdapter(Context ctxt, List<Event> events) {
        this.ctxt = ctxt;
        this.events = new ArrayList<>(events);
    }


    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

        String title = events.get(position).getTitle();
        String desc = events.get(position).getDescription();
        String date = events.get(position).getDate();

        holder.eventNameText.setText(title);
        holder.eventDescText.setText(desc);
        holder.eventDateText.setText(DateTimeParser.parseDateTime(date));

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void updateEventList(List<Event> new_events) {
        this.events = new_events;
        notifyDataSetChanged();
    }

    public void showDialog()
    {
        final Event event = events.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(ctxt);
        builder.setMessage("Do you want to add this event to your calendar?")
                .setTitle("Add to Calendar");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //add to calendar
                if (!isEventInCal(ctxt, event.getEventId() + ""))
                 //   addCalendarEvent(event);
                dialog.dismiss();
                Toast.makeText(ctxt,"Event added",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public boolean isEventInCal(Context context, String cal_meeting_id) {
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[] { "_id" }, " _id = ? ",
                new String[] { cal_meeting_id }, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }



   /* public void addCalendarEvent(Event event) {
        Calendar cal = Calendar.getInstance();
        Uri EVENTS_URI = Uri.parse(getCalendarUriBase(ctxt) + "events"); //creates a new uri for calendar
        ContentResolver cr = ctxt.getContentResolver();

        // event insert
        ContentValues values = new ContentValues();
        values.put("calendar_id", 1);
        values.put("_id",event.getEventId());
        values.put("title", event.getTitle());
        values.put("allDay", 0);
        values.put("dtstart", convertDateTimeToMilliseconds(event.getDate()));
        values.put("dtend",convertDateTimeToMilliseconds(event.getDate()));
        values.put("description", event.getDescription());
        values.put("hasAlarm", 1);
        values.put("eventTimezone", "UTC/GMT -5:00");
        Uri calevent = cr.insert(EVENTS_URI, values);

        // reminder insert
        Uri REMINDERS_URI = Uri.parse("content://com.android.calendar/" + "reminders");
        values = new ContentValues();
        ContentValues values2 = new ContentValues();
        ContentValues values3 = new ContentValues();

        values.put("event_id", Long.parseLong(calevent.getLastPathSegment()));
        values.put("method", 1);
        values.put("minutes", 10);

        values2.put("event_id", Long.parseLong(calevent.getLastPathSegment()));
        values2.put("method", 1);
        values2.put("minutes", 60);

        values3.put("event_id", Long.parseLong(calevent.getLastPathSegment()));
        values3.put("method", 1);
        values3.put("minutes", 60 * 24);

        try{
            cr.insert(REMINDERS_URI, values);
            cr.insert(REMINDERS_URI, values2);
            cr.insert(REMINDERS_URI, values3);
        }catch (SQLiteException s)
        {
            Toast.makeText(ctxt,"Error occured",Toast.LENGTH_SHORT).show();
        }

    }

    private String getCalendarUriBase(Context context) {
        Activity act = (Activity)context;
        String calendarUriBase = null;
        Uri calendars = Uri.parse("content://calendar/calendars");
        Cursor managedCursor = null;
        try {
            managedCursor = act.managedQuery(calendars, null, null, null, null);
        } catch (Exception e) {}
        if (managedCursor != null) {
            calendarUriBase = "content://calendar/";
        } else {
            calendars = Uri.parse("content://com.android.calendar/calendars");
            try {
                managedCursor = act.managedQuery(calendars, null, null, null, null);
            } catch (Exception e) {}
            if (managedCursor != null) {
                calendarUriBase = "content://com.android.calendar/";
            }
        }

        return calendarUriBase;
    }

    private long convertDateTimeToMilliseconds(String eventdate)
    {
        long timeInMilliseconds;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date mDate = sdf.parse(eventdate);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            return 0;
        }
        return timeInMilliseconds;
    }
*/

}


