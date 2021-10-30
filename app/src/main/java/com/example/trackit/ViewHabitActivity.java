package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewHabitActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    User user;
    Habit habit;
    TextView habitTitle;
    TextView habitReason;
    TextView startedDate;
    TextView repeatDays;
    Button editHabit;
    Button viewEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        habitTitle = findViewById(R.id.add_title);
        habitReason = findViewById(R.id.add_reason);
        startedDate = findViewById(R.id.add_start_date_text);
        repeatDays = findViewById(R.id.add_repeat_days);

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        startedDate.setText(habit.getStartDate());
        String textRepeat = "";
        for (int i = 0; i < habit.getRepeatDays().size(); i++)
        {
            if (i == 0)
                textRepeat = habit.getRepeatDays().get(i);
            else
                textRepeat = textRepeat + " " + habit.getRepeatDays().get(i);
        }
        repeatDays.setText(textRepeat);
    }
}
