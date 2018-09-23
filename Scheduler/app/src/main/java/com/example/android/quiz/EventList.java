package com.example.android.quiz;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rishi on 13/9/18.
 */

//the one event of the list of events
public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> events;

    public EventList(Activity context, List<Event> events) {
        super(context, R.layout.activity_event_list, events);
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_event_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewDetails = (TextView) listViewItem.findViewById(R.id.textViewDetails);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);

        Event event = events.get(position);
        textViewName.setText(event.getEventName());
        textViewDate.setText(event.getEventDate());
        textViewDetails.setText(event.getEventDetails());
        textViewTime.setText(event.getEventTime());

        return listViewItem;
    }
}

