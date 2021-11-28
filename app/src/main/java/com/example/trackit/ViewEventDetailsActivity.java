package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewEventDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 15;
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
    GoogleMap map;

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
//        else
//        {
//            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                    .findFragmentById(R.id.map);
//            mapFragment.getMapAsync(ViewEventDetailsActivity.this);
//        }

        if (image==null){
            noPhoto.setVisibility(View.VISIBLE);
            noPhoto.setText("This event has no uploaded photo.");
            imageView.setVisibility(View.GONE);
        }

//        TODO: Display saved location and uploaded image
    }

    public void editEvent (View view){
//        Intent intent = new Intent(ViewEventDetailsActivity.this, EditEventActivity.class);
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        // Prompt the user for permission.
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.getLatitude(),
//                event.getLongitude()), DEFAULT_ZOOM));
//        Marker currentMarker = map.addMarker(new MarkerOptions()
//                .position(new LatLng(event.getLatitude(),
//                        event.getLongitude())));

    }
}