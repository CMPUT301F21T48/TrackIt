package com.example.trackit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback{
    EditText comment;
    Button done;
    Bitmap image;
    GeoPoint location;
    Location curLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;


    // The entry point to the Places API.
    List<Address> addresses;


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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        Log.d(TAG, Integer.toString((int) curLocation.getLatitude()));
        Log.d(TAG, Integer.toString((int) curLocation.getLongitude()));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void fetchLastLocation() {
        //Check permission
        if (ActivityCompat.checkSelfPermission(AddEventActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    curLocation = task.getResult();
                }
            });
        }
        else {
            ActivityCompat.requestPermissions(AddEventActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = map;

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