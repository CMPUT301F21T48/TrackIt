package com.example.trackit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewEventDetailsActivity extends AppCompatActivity {

    User user;
    Habit habit;
    Event event;
    TextView date;
    TextView comment;
    TextView noLocation;
    TextView noPhoto;
    Double longitude;
    Double latitude;
    String image;
    ImageView imageView;
    LinearLayout mapHolder;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_details);

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        event = (Event) getIntent().getSerializableExtra("Event");

        date = findViewById(R.id.event_date);
        comment = findViewById(R.id.comment);
        noLocation = findViewById(R.id.no_location_text);
        noPhoto = findViewById(R.id.no_photo_text);
        imageView = findViewById(R.id.photo);
        mapHolder = findViewById(R.id.map_layout);

        date.setText("Created on: " + event.getEventDate());
        if (event.getComment().isEmpty()){
            comment.setText("This event has no comment.");
        } else{
            comment.setText(event.getComment());
        }

        latitude = event.getLatitude();
        longitude = event.getLongitude();
        image = event.getImage();

        if (latitude==null || longitude==null){
            noLocation.setVisibility(View.VISIBLE);
            noLocation.setText("This event has no recorded location.");
            mapHolder.setVisibility(View.GONE);
        }

        if (image==null){
            noPhoto.setVisibility(View.VISIBLE);
            noPhoto.setText("This event has no uploaded photo.");
            imageView.setVisibility(View.GONE);
        }

//        TODO: Display saved location and uploaded image
    }

    public void editEvent (View view){
//        Intent intent = new Intent(ViewEventDetailsActivity.this, EditEvent.class);
//        intent.putExtra("User", user);
//        intent.putExtra("Event", event);
//        intent.putExtra("Habit", habit);
//        startActivity(intent);
    }

    public void deleteEvent (View view){
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events");
        collectionReference.document(event.getEventID()).delete();
        finish();
    }
}