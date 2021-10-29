package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

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
    final CollectionReference collectionReference = db.collection("Users");
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        user = (User) getIntent().getSerializableExtra("User");

        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);
        addHabitButton = findViewById(R.id.button_add_habit);
        cancelButton = findViewById(R.id.button_cancel);
        addHabitReason = findViewById(R.id.add_reason);
        addHabitTitle = findViewById(R.id.add_title);
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
        String habitTitle = addHabitTitle.getText().toString();
        String habitReason = addHabitReason.getText().toString();
        String habitStartDate = addStartDate;
        ArrayList<String> repeatDays = new ArrayList<>();

        Habit habit = new Habit(habitTitle, habitReason, habitStartDate, repeatDays);

        String id = collectionReference.document(user.getUsername()).collection("Habits").document().getId();

        habit.setHabitID(id);

        collectionReference.document(user.getUsername()).collection("Habits").document(id).set(habit);

    }

    public void cancelAddHabit(View view) {
        finish();
    }
}