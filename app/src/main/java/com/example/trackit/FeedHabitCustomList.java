package com.example.trackit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FeedHabitCustomList extends ArrayAdapter<FeedHabit> {

    private final ArrayList<FeedHabit> habits;
    private final Context context;
    private FeedHabit habit;
    private User user;

    // constructor
    public FeedHabitCustomList(Context context, ArrayList<FeedHabit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.feed_habit, parent, false);
        }

        habit = habits.get(position);

        TextView habitTitle = view.findViewById(R.id.habit_title_display);
        TextView habitReason = view.findViewById(R.id.habit_reason_display);
        ProgressBar habitProgress = view.findViewById(R.id.habit_progress);
        TextView username = view.findViewById(R.id.user_text);

        habitTitle.setText(habit.getTitle());
        habitReason.setText("Reason: " + habit.getReason());
        habitProgress.setProgress((int) Math.round(habit.getProgress() * 100));
        username.setText("User: " + habit.getUsername());

        return view;
    }
}
