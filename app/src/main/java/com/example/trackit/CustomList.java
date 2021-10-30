package com.example.trackit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Habit> {

    private final ArrayList<Habit> habits;
    private final Context context;

    // constructor
    public CustomList(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.habit, parent, false);
        }

        Habit habit = habits.get(position);

        TextView habitTitle = view.findViewById(R.id.habit_title_display);
        TextView habitRepeat = view.findViewById(R.id.habit_repeat_display);
        TextView habitReason = view.findViewById(R.id.habit_reason_display);
        TextView habitProgress = view.findViewById(R.id.habit_progress);

        habitTitle.setText(habit.getTitle());
        String repeatDays = "";
        for (int i = 0; i < habit.getRepeatDays().size(); i++){
            repeatDays = repeatDays + " " + habit.getRepeatDays().get(i);
        }
        habitRepeat.setText("Repeat:" + repeatDays);
        habitReason.setText("Reason: " + habit.getReason());
        habitProgress.setText("Progress: " + Math.round(habit.getProgress()) + "%");

        LinearLayout habitMenu = view.findViewById(R.id.habit_menu);
        LinearLayout habitData = view.findViewById(R.id.habit_data);
        TextView viewDetails = view.findViewById(R.id.view_details);

        final boolean[] isClicked = {false};
        habitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked[0]) {
                    habitMenu.setVisibility(View.VISIBLE);
                    isClicked[0] = true;
                } else {
                    habitMenu.setVisibility(View.GONE);
                    isClicked[0] = false;
                }
            }
        });

//        viewDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isClicked[0]) {
//                    habitMenu.setVisibility(View.VISIBLE);
//                    isClicked[0] = true;
//                } else {
//                    habitMenu.setVisibility(View.GONE);
//                    isClicked[0] = false;
//                }
//            }
//        });

        return view;
    }
}
