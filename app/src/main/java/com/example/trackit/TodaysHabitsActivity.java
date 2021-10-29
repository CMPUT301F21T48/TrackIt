package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class TodaysHabitsActivity extends AppCompatActivity {

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_habits);

        User user = (User) getIntent().getSerializableExtra("User");

        Button addButton = findViewById(R.id.add_button);
        Button editButton = findViewById(R.id.edit_button);

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(TodaysHabitsActivity.this, AddHabitActivity.class);
                intent.putExtra("User", (Serializable) user);
                startActivity(intent);
            }
        });

    }
}