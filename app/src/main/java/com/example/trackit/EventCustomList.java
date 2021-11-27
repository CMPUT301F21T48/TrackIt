package com.example.trackit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EventCustomList extends ArrayAdapter<Event> {
    private final ArrayList<Event> events;
    private final Context context;
    private Event event;

    public EventCustomList(Context context, ArrayList<Event> events) {
        super(context,0, events);
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.event, parent, false);
        }

        event = events.get(position);

        TextView eventTitle = view.findViewById(R.id.event_details);
        eventTitle.setText(event.getEventDate());

        return view;
    }



}
