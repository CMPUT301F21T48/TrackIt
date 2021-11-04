package com.example.trackit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditHabitActivity extends AppCompatActivity {

    EditText editHabitTitle;
    EditText editHabitReason;
    Button editHabitButton;
    Button cancelButton;
    String habitID;
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

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        habitID = habit.getHabitID();
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
            if (repeatDays.get(i).equals("M"))
                repeatMonday.setChecked(true);
            else if (repeatDays.get(i).equals("T"))
                repeatTuesday.setChecked(true);
            else if (repeatDays.get(i).equals("W"))
                repeatWednesday.setChecked(true);
            else if (repeatDays.get(i).equals("R"))
                repeatThursday.setChecked(true);
            else if (repeatDays.get(i).equals("F"))
                repeatFriday.setChecked(true);
            else if (repeatDays.get(i).equals("S"))
                repeatSaturday.setChecked(true);
            else if (repeatDays.get(i).equals("Su"))
                repeatSunday.setChecked(true);
        }
    }

    public void selectDate(View view) {
        // Retrieve and set selected start date
        String date = Integer.toString(datePicker.getDayOfMonth());
        if (Integer.valueOf(date) < 10) {
            date = "0" + date;
        }
        String month = Integer.toString(datePicker.getMonth() + 1);
        if (Integer.valueOf(month) < 10) {
            month = "0" + month;
        }
        String year = Integer.toString(datePicker.getYear());
        addStartDate = date + "/" + month + "/" + year;
        selectedDate.setText(addStartDate);
    }

    public void saveChanges(View view) {
        String habitTitle = editHabitTitle.getText().toString();
        String habitReason = editHabitReason.getText().toString();
        String habitStartDate = selectedDate.getText().toString();
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

        if (habitTitle.isEmpty() || habitReason.isEmpty() || habitStartDate.equals("MM/DD/YYYY") || repeatDays.isEmpty()) {
            Snackbar.make(this, view, "Do not leave any field(s) empty", Snackbar.LENGTH_LONG).show();
        }
        else {
            habit.setTitle(habitTitle);
            habit.setReason(habitReason);
            habit.setStartDate(habitStartDate);
            habit.setRepeatDays(repeatDays);
            collectionReference.document(user.getUsername()).collection("Habits").document(habitID).set(habit);
            finish();
        }
    }

    public void cancelAddHabit(View view) { finish(); }
}