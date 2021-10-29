package com.example.trackit;

<<<<<<< Updated upstream
import android.widget.TextView;

=======
>>>>>>> Stashed changes
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

<<<<<<< Updated upstream
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


    
=======
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

        habitTitle.setText(habit.getTitle());
        String repeatDays = "";
        for (int i = 0; i < habit.getRepeatDays().size(); i++){
            repeatDays = repeatDays + " " + habit.getRepeatDays().get(i);
        }
        habitRepeat.setText("Repeat: " + repeatDays);
        habitReason.setText("Reason: " + habit.getReason());

        return view;
    }
}
>>>>>>> Stashed changes
