package com.example.trackit;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener {
    private EditText comment;
    private Button recordLocation;
    private Button photo_button;
    private TextView locationText;
    private LinearLayout locationLayout;
    private Bitmap image;
    private GeoPoint location;
    private Location curLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap map;
    private boolean locationPermissionGranted;
    private boolean cameraPermissionGranted;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final LatLng defaultLocation = new LatLng(53.5461, 113.4938);
    private static final int DEFAULT_ZOOM = 15;
    private Marker currentMarker;
    private Context ImageContext;
    private String encodedPhoto;
    private Integer MAX_IMAGE_BYTE = 65536;

    //Request codes
    public static final int REQUEST_CODE = 100;
    public static final int CAMERA_REQUEST = 9999;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private ImageView imageView;

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
        recordLocation = findViewById(R.id.button_location);
        locationText = findViewById(R.id.location);
        locationLayout = findViewById(R.id.locationLayout);
        photo_button = findViewById(R.id.button_photo);
        imageView = findViewById(R.id.photo);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkAndRequestPermissions();
        //getting the map
        recordLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestPermissions();
                if (locationPermissionGranted) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(AddEventActivity.this);
                    locationLayout.setVisibility(View.VISIBLE);
                    recordLocation.setVisibility(View.INVISIBLE);
                    locationText.setText("Drag and drop the marker to set location");
                }
                else {
                    Toast.makeText(AddEventActivity.this, "Please grant permission to access your current location.", Toast.LENGTH_SHORT).show();

                }
            }
        });
        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();

                if (cameraPermissionGranted) {

                    Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (photoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(photoIntent, CAMERA_REQUEST);
                    }
                }
                else {
                    Toast.makeText(AddEventActivity.this, "Please grant permission to access your camera.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageContext = getApplicationContext();
        imageView.setImageBitmap(null);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK && data != null)  {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView.setImageBitmap(imageBitmap);
            //this.image = imageBitmap;
            imageView.setVisibility(View.VISIBLE);
            if ( ((ImageView) findViewById(R.id.photo)).getDrawable() != null ) {
                Bitmap pic = ((BitmapDrawable) ((ImageView) findViewById(R.id.photo)).getDrawable()).getBitmap();
                encodeBitmapAndResize(pic);
            }


        }

//        } else if (requestCode == RESULT_CANCELED) {
//            // User cancelled the image capture
//        } else {
//            // Image capture failed, advise user
//        }
    }

    public void encodeBitmapAndResize(Bitmap bitmap) {

        if (bitmap.getByteCount() > MAX_IMAGE_BYTE) {
            for (int i = 0; i < 4; ++i) {
                bitmap = resizeImage(bitmap);
                if (bitmap.getByteCount() <= MAX_IMAGE_BYTE) {
                    break;
                }
            }
        }

        if (bitmap.getByteCount() <= MAX_IMAGE_BYTE) {

            this.image = bitmap;

            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
            this.encodedPhoto = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);

        } else {
            throw new IllegalArgumentException("Image file must be less than or equal to " +
                    String.valueOf(MAX_IMAGE_BYTE) + " bytes.");
        }


    }
    private void decodePhoto() {
        if (this.encodedPhoto != null) {
            byte [] decodeBytesArray = Base64.decode(this.encodedPhoto, 0);
            this.image= BitmapFactory.decodeByteArray(decodeBytesArray, 0, decodeBytesArray.length);
        }
    }
    private Bitmap resizeImage(Bitmap bitmap) {
        double downScale = 0.95;
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * downScale), (int) (bitmap.getHeight() * downScale), true);
    }


    public void deletePhoto() {
        this.image= null;
        this.encodedPhoto = null;
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
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    public void done(View view) {
        Event event = new Event();
        if (!comment.getText().equals(""))
        {
            event.setComment(comment.getText().toString());
        }
        if (image != null)
        {
            event.setImage(encodedPhoto);
        }
        if (!locationPermissionGranted)
        {
            location = null;
        }
        event.setLocation(location);
        String id = collectionReference.document(user.getUsername())
                .collection("Habits").document(habit.getHabitID())
                .collection("Events").document().getId();
        event.setEventID(id);
        collectionReference.document(user.getUsername()).collection("Habits")
                .document(habit.getHabitID()).collection("Events")
                .document(id).set(event);
        finish();
    }

    public void skip(View view) {
        finish();
    }
}