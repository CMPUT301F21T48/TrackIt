package com.example.trackit;

import android.content.Context;


import java.util.ArrayList;

public class EventCustomList {
    private final ArrayList<Event> events;
    private final Context context;
    private Event event;

    public EventCustomList(Context context, ArrayList<Event> events) {
        this.events = events;
        this.context = context;
    }



}
