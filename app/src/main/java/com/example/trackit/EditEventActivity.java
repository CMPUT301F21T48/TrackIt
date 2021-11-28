package com.example.trackit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener {

    User user;
    Habit habit;
    Event event;
    Double longitude;
    Double latitude;
    String image;
    ImageView imageView;
    LinearLayout mapHolder;
    GoogleMap map;
    EditText commentText;
    Marker currentMarker;
    Boolean locationPermissionGranted;
    Boolean cameraPermissionGranted;
    private GeoPoint location;
    private Location curLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final LatLng defaultLocation = new LatLng(0, 0);
    private static final float DEFAULT_ZOOM = 15;

    Button removeLocation, removePhoto, changeLocation, changePhoto;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        event = (Event) getIntent().getSerializableExtra("Event");

        imageView = findViewById(R.id.photo);
        mapHolder = findViewById(R.id.locationLayout);

        removeLocation = findViewById(R.id.button_remove_location);
        removePhoto = findViewById(R.id.button_remove_photo);
        changeLocation = findViewById(R.id.button_location);
        changePhoto = findViewById(R.id.button_photo);

        commentText = findViewById(R.id.add_comment);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkAndRequestPermissions();


        if (!event.getComment().isEmpty()){
            commentText.setText(event.getComment());
        }

        latitude = event.getLatitude();
        longitude = event.getLongitude();
        image = event.getImage();

        if (latitude==null || longitude==null){
            removeLocation.setVisibility(View.GONE);
        }

        removeLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 event.setLongitude(null);
                 event.setLatitude(null);
                 removeLocation.setVisibility(View.GONE);
             }
         });

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                changeLocation.setVisibility(View.GONE);
                removeLocation.setVisibility(View.GONE);
                mapHolder.setVisibility(View.VISIBLE);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(EditEventActivity.this);
            }
        });

        if (image==null){
            imageView.setVisibility(View.GONE);
            removePhoto.setVisibility(View.GONE);
        }
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
    public void onMarkerDrag(@NonNull Marker marker) { }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        LatLng position = currentMarker.getPosition();
        location = new GeoPoint(position.latitude, position.longitude);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) { }

    private void checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
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
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new
                    String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
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
                locationResult.addOnCompleteListener(this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    // Set the map's camera position to the current location of the device.
                                    curLocation = task.getResult();
                                    if (curLocation != null) {
                                        if (event.getLatitude() != null) {
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(event.getLatitude(),
                                                            event.getLongitude()), DEFAULT_ZOOM));
                                            currentMarker = map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(event.getLatitude(),
                                                            event.getLongitude()))
                                                    .draggable(true));
                                            location = new GeoPoint(event.getLatitude(),
                                                    event.getLongitude());
                                        }
                                        else {
                                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(curLocation.getLatitude(),
                                                            curLocation.getLongitude()), DEFAULT_ZOOM));
                                            currentMarker = map.addMarker(new MarkerOptions()
                                                    .position(new LatLng(curLocation.getLatitude(),
                                                            curLocation.getLongitude()))
                                                    .draggable(true));
                                            location = new GeoPoint(curLocation.getLatitude(),
                                                    curLocation.getLongitude());
                                        }
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
            else
            {
                currentMarker = map.addMarker(new MarkerOptions()
                        .position(defaultLocation)
                        .draggable(true));
                location = new GeoPoint (0,0);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    public void done(View view) {
        event.setComment(commentText.getText().toString());
        if (location != null) {
            event.setLatitude(location.getLatitude());
            event.setLongitude(location.getLongitude());
        }
        collectionReference.document(user.getUsername()).collection("Habits")
                .document(habit.getHabitID()).collection("Events")
                .document(event.getEventID()).set(event);
//        finish();
    }
}