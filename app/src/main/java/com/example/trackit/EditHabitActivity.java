package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditHabitActivity extends AppCompatActivity {

    EditText editHabitTitle;
    EditText editHabitReason;
    Button editHabitButton;
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
    final CollectionReference collectionReference = db.collection("Users");
    User user;
    Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("user");
        habit = (Habit) getIntent().getSerializableExtra("habit");
        setContentView(R.layout.activity_edit_habit);
        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);
        editHabitButton = findViewById(R.id.button_edit_habit);
        cancelButton = findViewById(R.id.button_cancel);
        editHabitReason = findViewById(R.id.add_reason);
        editHabitTitle = findViewById(R.id.add_title);
        repeatMonday = findViewById(R.id.checkbox_monday);
        repeatTuesday = findViewById(R.id.checkbox_tuesday);
        repeatWednesday = findViewById(R.id.checkbox_wednesday);
        repeatThursday = findViewById(R.id.checkbox_thursday);
        repeatFriday = findViewById(R.id.checkbox_friday);
        repeatSaturday = findViewById(R.id.checkbox_saturday);
        repeatSunday = findViewById(R.id.checkbox_sunday);

        //adding the previous information for habit in the fields
        editHabitTitle.setText(habit.getTitle());
        editHabitReason.setText(habit.getReason());
        selectedDate.setText(habit.getStartDate());
        Integer year = Integer.parseInt(habit.getStartDate().substring(6,10));
        Integer month = Integer.parseInt(habit.getStartDate().substring(3,5))-1;
        Integer day = Integer.parseInt(habit.getStartDate().substring(0,2));
        datePicker.updateDate(day,month,year);
        ArrayList<String> repeatDays = habit.getRepeatDays();
        for (int i = 0; i < repeatDays.size(); i++)
        {
            if (repeatDays.get(i) == "M")
                repeatMonday.setChecked(true);
            if (repeatDays.get(i) == "T")
                repeatTuesday.setChecked(true);
            if (repeatDays.get(i) == "W")
                repeatWednesday.setChecked(true);
            if (repeatDays.get(i) == "R")
                repeatThursday.setChecked(true);
            if (repeatDays.get(i) == "F")
                repeatFriday.setChecked(true);
            if (repeatDays.get(i) == "S")
                repeatSaturday.setChecked(true);
            if (repeatDays.get(i) == "Su")
                repeatSunday.setChecked(true);
        }
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

    public void saveChanges(View view) {
        String habitTitle = editHabitTitle.getText().toString();
        String habitReason = editHabitReason.getText().toString();
        String habitStartDate = addStartDate;
        ArrayList<String> repeatDays = new ArrayList<>();

        if (repeatMonday.isChecked()){
            repeatDays.add("M");
        }
        if (repeatTuesday.isChecked()){
            repeatDays.add("T");
        }
        if (repeatWednesday.isChecked()){
            repeatDays.add("W");
        }
        if (repeatThursday.isChecked()){
            repeatDays.add("R");
        }
        if (repeatFriday.isChecked()){
            repeatDays.add("F");
        }
        if (repeatSaturday.isChecked()){
            repeatDays.add("S");
        }
        if (repeatSunday.isChecked()){
            repeatDays.add("Su");
        }

        habit.setTitle(habitTitle);
        habit.setReason(habitReason);
        habit.setStartDate(habitStartDate);
        habit.setRepeatDays(repeatDays);
        String id = habit.getHabitID();
        collectionReference.document(user.getUsername()).collection("Habits").document(id).set(habit);

        finish();
    }
    public void cancelAddHabit(View view) {
        finish();
    }

}