package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class AddEventActivity extends AppCompatActivity {
    EditText comment;
    Button done;
    Bitmap image;
    GeoPoint location;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    User user;
    Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        comment = findViewById(R.id.add_comment);
    }

    public void addLocation(View view) {

    }

    public void addPhoto(View view) {

    }

    public void done(View view) {
        Event event = new Event();
        if (comment.getText() != null)
        {
            event.setComment(comment.getText().toString());
        }
        if (location != null)
        {
            event.setLocation(location);
        }
        if (image != null)
        {
            event.setImage(image);
        }
        String id = collectionReference.document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events").document().getId();
        event.setEventID(id);
        collectionReference.document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events").document(id).set(event);
        finish();
    }
}