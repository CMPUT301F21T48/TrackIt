package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddHabitActivity extends AppCompatActivity {

    EditText addHabitTitle;
    EditText addHabitReason;
    Button addHabitButton;
    Button cancelButton;
    DatePicker datePicker;
    TextView selectedDate;
    String addStartDate;
    CheckBox repeatMonday;
    CheckBox repeatTuesday;
    CheckBox repeatWednesday;
    CheckBox repeatThursday;
    CheckBox repeatFriday;
    CheckBox repeatSaturday;
    CheckBox repeatSunday;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);
        addHabitButton = findViewById(R.id.button_add_habit);
        cancelButton = findViewById(R.id.button_cancel);
        addHabitReason = findViewById(R.id.add_reason);
        addHabitTitle = findViewById(R.id.add_title);

        Intent intent = getIntent();
    }

    public void selectDate(View view) {
        // Retrieve and set selected start date
        String date = Integer.toString(datePicker.getDayOfMonth());
        String month = Integer.toString(datePicker.getMonth() + 1);
        if (Integer.valueOf(month) < 10) {
            month = "0" + month;
        }
        String year = Integer.toString(datePicker.getYear());
        addStartDate = month + "/" + date + "/" + year;
        selectedDate.setText(addStartDate);
    }

    public void addHabit(View view) {
        // Add habit to database and user profile
        finish();
    }

    public void cancelAddHabit(View view) {
        finish();
    }
}