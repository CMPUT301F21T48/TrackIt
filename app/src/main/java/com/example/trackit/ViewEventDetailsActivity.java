package com.example.trackit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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

    /**
     * This method is called when the activity is created.
     * It gets the user, habit and event and all the textviews and imageviews
     * Then sets the location, date, comment and image in the imageview to display the event
     * If the previous location was not provided it is not displayed
     * If there was no photograph for the event it is not displayed
     * @param savedInstanceState - previous state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_details);

        // Get the user and habit and event from the intent
        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        event = (Event) getIntent().getSerializableExtra("Event");

        // Get the text views and imageviews from the layout
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
            //no map is displayed if no previous location provided
            noLocation.setVisibility(View.VISIBLE);
            noLocation.setText("This event has no recorded location.");
            mapHolder.setVisibility(View.GONE);
        }
        else
        {
            //initializing the map is location was previously provided
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(ViewEventDetailsActivity.this);
        }

        if (image==null){
            // no image is displayed if there was no uploaded photo previously
            noPhoto.setVisibility(View.VISIBLE);
            noPhoto.setText("This event has no uploaded photo.");
            imageView.setVisibility(View.GONE);
        }
        else {
            // decode the photo and set the imageview
            Bitmap picture = decodePhoto(image);
            imageView.setImageBitmap(picture);
            imageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Decodes the encoded image and returns it as a bitmap.
     * @param encodedPhoto - the encoded photo
     * @return decoded bitmap of photo
     */
    private Bitmap decodePhoto(String encodedPhoto) {
        byte [] decodeBytesArray = Base64.decode(encodedPhoto, 0);
        return BitmapFactory.decodeByteArray(decodeBytesArray, 0, decodeBytesArray.length);
    }

    /**
     *
     * @param view - the view of the activity
     */
    public void editEvent (View view){
        Intent intent = new Intent(ViewEventDetailsActivity.this, EditEventActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("Event", event);
        intent.putExtra("Habit", habit);
        startActivity(intent);
        finish();
    }

    /**
     * Deletes the event from firebase and returns to the previous activity.
     * @param view - the view of the activity
     */
    public void deleteEvent (View view){
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events");
        collectionReference.document(event.getEventID()).delete();
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;
        // Setting the marker to previous location
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(event.getLatitude(),
                event.getLongitude()), DEFAULT_ZOOM));
        Marker currentMarker = map.addMarker(new MarkerOptions()
                .position(new LatLng(event.getLatitude(),
                        event.getLongitude())));

    }
}