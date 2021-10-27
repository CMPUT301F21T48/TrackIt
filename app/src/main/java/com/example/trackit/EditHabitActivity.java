package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class EditHabitActivity extends AppCompatActivity {

    EditText editHabitTitle;
    EditText editHabitReason;
    Button editHabitButton;
    Button cancelButton;
    DatePicker datePicker;
    TextView selectedDate;
    String addStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Habit habit = (Habit) getIntent().getSerializableExtra("habit");
        setContentView(R.layout.activity_edit_habit);
        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);
        editHabitButton = findViewById(R.id.button_edit_habit);
        cancelButton = findViewById(R.id.button_cancel);
        editHabitReason = findViewById(R.id.add_reason);
        editHabitTitle = findViewById(R.id.add_title);

        //adding the previous information for habit in the fields
        editHabitTitle.setText(habit.getTitle());
        editHabitReason.setText(habit.getReason());
        selectedDate.setText(habit.getStartDate());
        Integer year = Integer.parseInt(habit.getStartDate().substring(6,10));
        Integer month = Integer.parseInt(habit.getStartDate().substring(3,5))-1;
        Integer day = Integer.parseInt(habit.getStartDate().substring(0,2));
        datePicker.updateDate(day,month,year);
    }

    public void selectDate(View view) {
        // Retrieve and set selected start date
        String date = Integer.toString(datePicker.getDayOfMonth());
        String month = Integer.toString(datePicker.getMonth() + 1);
        if (Integer.valueOf(month) < 10) {
            month = "0" + month;
        }
        String year = Integer.toString(datePicker.getYear());
        addStartDate = date + "-" + month + "-" + year;
        selectedDate.setText(addStartDate);
    }
}