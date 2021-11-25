package com.example.trackit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener {
    private EditText comment;
    private Button recordLocation;
    private TextView locationText;
    private LinearLayout locationLayout;
    private Button done;
    private Bitmap image;
    private GeoPoint location;
    private Location curLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private boolean locationPermissionGranted;
    private boolean cameraPermissionGranted;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
//    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Marker currentMarker;

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
        checkAndRequestPermissions();
        Log.d(TAG, String.valueOf(locationPermissionGranted));
        comment = findViewById(R.id.add_comment);
        recordLocation = findViewById(R.id.button_location);
        locationText = findViewById(R.id.location);
        locationLayout = findViewById(R.id.locationLayout);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //        getCameraPermission();
        //getting the map
        recordLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(AddEventActivity.this);
                locationLayout.setVisibility(View.VISIBLE);
                recordLocation.setVisibility(View.INVISIBLE);
                locationText.setText("Drag and drop the marker to set location");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        // Prompt the user for permission.
        updateLocationUI();
        getDeviceLocation();
        map.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        LatLng position = currentMarker.getPosition();
        location = new GeoPoint(position.latitude, position.longitude);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

//    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            locationPermissionGranted = true;
//        }
//        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
//                Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED) {
//            cameraPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.CAMERA},
//                    MY_CAMERA_REQUEST_CODE);
//        }
//    }
    private void checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else
        {
            locationPermissionGranted = true;
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        else
        {
            cameraPermissionGranted = true;
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                curLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            curLocation = task.getResult();
                            if (curLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(curLocation.getLatitude(),
                                                curLocation.getLongitude()), DEFAULT_ZOOM));
                                currentMarker = map.addMarker(new MarkerOptions()
                                        .position(new LatLng(curLocation.getLatitude(),
                                                curLocation.getLongitude()))
                                        .draggable(true));
                                location = new GeoPoint (curLocation.getLatitude(), curLocation.getLongitude());

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void addPhoto(View view) {
        Log.d(TAG, String.valueOf(cameraPermissionGranted));
    }

    public void done(View view) {
        Event event = new Event();
        if (!comment.getText().equals(""))
        {
            event.setComment(comment.getText().toString());
        }
        if (image != null)
        {
            event.setImage(image);
        }
        event.setLocation(location);
        String id = collectionReference.document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events").document().getId();
        event.setEventID(id);
        collectionReference.document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events").document(id).set(event);
        finish();
    }

    public void skip(View view) {
        finish();
    }
}