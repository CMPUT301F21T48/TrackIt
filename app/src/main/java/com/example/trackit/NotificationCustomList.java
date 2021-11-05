package com.example.trackit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NotificationCustomList extends ArrayAdapter<User> {

    private final ArrayList<User> users;
    private final Context context;
    private User user;

    // constructor
    public NotificationCustomList(@NonNull Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.notification, parent, false);
        }

        user = users.get(position);

        TextView notificationMessage = view.findViewById(R.id.notification_message);

        notificationMessage.setText(user.getUsername() + " has requested to follow you.");

        return view;
    }
}
