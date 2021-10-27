package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class TodaysHabitsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_habits);
        ArrayList<String> days = new ArrayList<String>();
        days.add("Wednesday");
        Habit habit = new Habit("sleep at night", "get enough sleep", "12-10-2021", days);

        Button addButton = findViewById(R.id.add_button);
        Button editButton = findViewById(R.id.edit_button);

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TodaysHabitsActivity.this, EditHabitActivity.class);
                intent.putExtra("habit", (Parcelable) habit);
                startActivity(intent);
            }
        });

    }
}