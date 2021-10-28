package com.example.trackit;

import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter {
    
        private ArrayList<Habit> habits;
        private Context context;

        public CustomList(Context context, ArrayList<Habit> Habits){
            super(context,0, Habits);
            this.habits = Habits;
            this.context = context;
        }

        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
                parent) {
            // return super.getView(position, convertView, parent);
            View view = convertView;
            if(view == null){
                view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
            }
            Habit Habit = habits.get(position);
            TextView habitName = view.findViewById(R.id.habit_title);
            TextView habitReason = view.findViewById(R.id.description);
            TextView habitDate = view.findViewById(R.id.days);
            TextView progress = view.findViewById(R.id.progress);
            habitName.setText(Habit.getTitle());
            //habitDate.setText((CharSequence) Habit.getStartDate());
            habitDate.setText( Habit.getRepeatDays().toString());
            habitReason.setText(Habit.getReason().toString());
            progress.setText(Habit.getProgress());
            return view;
        }


    }


    
