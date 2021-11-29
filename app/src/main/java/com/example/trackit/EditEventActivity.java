package com.example.trackit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class EditEventActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener {

    User user;
    Habit habit;
    Event event;
    Double longitude;
    Double latitude;
    String encodedImage;
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
    private final LatLng defaultLocation = new LatLng(53.5461, -113.4938);
    private static final float DEFAULT_ZOOM = 15;
    public static final int CAMERA_REQUEST = 9999;
    private Bitmap newImage;
    private String newEncodedImage;
    private Boolean photoChange = false;

    Button removeLocation, removePhoto, changeLocation, changePhoto;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    /**
     * Initializes the activity and records click listeners for the buttons for maps and camera
     * If remove buttons are clicked it removes the location or photo accordingly
     * If change button for location is clicked it changes the location
     * If change button for photo is clicked it starts the camera to change the image
     * @param savedInstanceState - previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        collectionReference = db.collection("Users");

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        event = (Event) getIntent().getSerializableExtra("Event");

        //initializing all the buttons and EditViews
        imageView = findViewById(R.id.photo);
        mapHolder = findViewById(R.id.locationLayout);

        removeLocation = findViewById(R.id.button_remove_location);
        removePhoto = findViewById(R.id.button_remove_photo);
        changeLocation = findViewById(R.id.button_location);
        changePhoto = findViewById(R.id.button_photo);

        commentText = findViewById(R.id.add_comment);

        //initializing the client for getting current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //getting camera and location permission
        checkAndRequestPermissions();


        if (!event.getComment().isEmpty()){
            commentText.setText(event.getComment());
        }

        latitude = event.getLatitude();
        longitude = event.getLongitude();
        encodedImage = event.getImage();

        //if no location is given in event, setting the remove button to invisible
        if (latitude==null || longitude==null){
            removeLocation.setVisibility(View.INVISIBLE);
        }

        //remove location sets the latitude and longgitude to null
        removeLocation.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 event.setLongitude(null);
                 event.setLatitude(null);
                 removeLocation.setVisibility(View.INVISIBLE);
             }
         });

        //to change location map is loaded with previous location if any
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(EditEventActivity.this);
                changeLocation.setVisibility(View.INVISIBLE);
                removeLocation.setVisibility(View.INVISIBLE);
                mapHolder.setVisibility(View.VISIBLE);
            }
        });

        if (encodedImage == null) {
            removePhoto.setVisibility(View.INVISIBLE);
        }
        // removes photo and sets image to null
        removePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setImage(null);
                removePhoto.setVisibility(View.INVISIBLE);
            }
        });
        //to change photo camera is started
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoChange = true;
                checkAndRequestPermissions();
                removePhoto.setVisibility(View.INVISIBLE);
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (photoIntent.resolveActivity(getPackageManager()) != null) {
                    photoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(photoIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    /***
     * This method get photos from the photo activity and sets the image to the image view
     * @param requestCode This is the integer request code allowing the app to identify the activity
     * @param resultCode This is the integer result code returned by the activity
     * @param data This is the result data returned by the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //ImageContext = getApplicationContext();
        imageView.setImageBitmap(null);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null)  {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Downscale the image so that it fits in Firestore
            imageBitmap = downscaleBitmap(imageBitmap);
            this.newImage = imageBitmap;

            //Set the imageview to the image
            imageView.setImageBitmap(imageBitmap);
            imageView.setVisibility(View.VISIBLE);

            //Encode the image to a string
            if ( ((ImageView) findViewById(R.id.photo)).getDrawable() != null ) {
                Bitmap pic = ((BitmapDrawable) ((ImageView) findViewById(R.id.photo)).getDrawable()).getBitmap();
                encodeBitmapAndResize(pic);
            }
        }
    }

    /**
     * This method downscales the image bitmap so that it fits in Firestore
     * @param pic Bitmap image to be downscaled
     * @return downscaled bitmap
     */
    private Bitmap downscaleBitmap(Bitmap pic) {
        double maxDimension = Math.max(pic.getHeight(), pic.getWidth());
        double scale = 275 / maxDimension;
        return Bitmap.createScaledBitmap(pic, (int) (pic.getWidth() * scale), (int) (pic.getHeight() * scale), false);
    }

    /**
     * This method encodes the bitmap to a string
     * @param bitmap Bitmap image to be encoded
     */
    public void encodeBitmapAndResize(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 64, byteArrayOS);
        this.encodedImage = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        //initializes a marker and sets to a defualt location
        currentMarker = map.addMarker(new MarkerOptions()
                .position(defaultLocation)
                .draggable(true));
        //setting marker to previously provided location
        if (event.getLatitude() != null && event.getLongitude() != null)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(event.getLatitude(),
                            event.getLongitude()), DEFAULT_ZOOM));
            currentMarker.setPosition(new LatLng(event.getLatitude(),
                            event.getLongitude()));
            location = new GeoPoint(event.getLatitude(),
                    event.getLongitude());
            updateLocationUI();
        }
        else{
            //if no previous location provided, setting marker to current location
            // (if permission is granted to access current location)
            updateLocationUI();
            getDeviceLocation();
        }
    map.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) { }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        //getting the position when marker is stopped dragging
        LatLng position = currentMarker.getPosition();
        location = new GeoPoint(position.latitude, position.longitude);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) { }

    /**
     * This method checks if the permissions are granted and requests them if they are not
     */
    private void checkAndRequestPermissions() {
        //getting camera and location permissions
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
            locationPermissionGranted = false;
        }
        else
        {
            locationPermissionGranted = true;
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
            cameraPermissionGranted = false;
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
                //setting the focus on current location to true is permission granted
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
            //if permission granted, getting the current location, else setting
            // marker to default location
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
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(curLocation.getLatitude(),
                                                        curLocation.getLongitude()), DEFAULT_ZOOM));
                                        currentMarker = map.addMarker(new MarkerOptions()
                                                .position(new LatLng(curLocation.getLatitude(),
                                                        curLocation.getLongitude()))
                                                .draggable(true));
                                        location = new GeoPoint (curLocation.getLatitude(),
                                                curLocation.getLongitude());

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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        defaultLocation, DEFAULT_ZOOM));
                location = new GeoPoint (defaultLocation.latitude, defaultLocation.longitude);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * This method is called when the user clicks the submit button and sets latitude, longitude and image
     * @param view
     */
    public void done(View view) {
        //editing the required fields and updating the event in firebase
        event.setComment(commentText.getText().toString());
        if (location != null) {
            event.setLatitude(location.getLatitude());
            event.setLongitude(location.getLongitude());
        }
        if (photoChange)
        {
            event.setImage(encodedImage);
        }

        collectionReference.document(user.getUsername()).collection("Habits")
                .document(habit.getHabitID()).collection("Events")
                .document(event.getEventID()).set(event);
        finish();
    }

    /**
     * This method is called when the user clicks the skip button and finishes the activity
     * @param view - instance of view
     */
    public void skip(View view) { finish(); }
}