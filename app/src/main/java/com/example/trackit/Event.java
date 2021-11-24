package com.example.trackit;

import android.graphics.Bitmap;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

/**
 * Habit is the object representing a habit type, belonging to a specific user.
 * It consists of a name, reason, schedule of days of the week it should be repeated, the date to start, and
 * a progress bar which will increase each time the habit is done.
 * The Habit performs some calculations for how many times it was done on schedule, and how many
 * times it was not done.
 */

public class Event implements Serializable {
    private String comment;
    private GeoPoint location;
    private Bitmap image;
    private String ID;

    public Event()
    {
        this.comment = null;
        this.image = null;
        this.location = null;
        this.ID = null;
    }

    //getters and setters for the variables
    public void setComment(String comment) {this.comment = comment;}
    public void setLocation(GeoPoint location) {this.location = location;}
    public void setImage(Bitmap image) {this.image = image;}
    public void setEventID(String ID) {this.ID = ID;}

    public String getComment() {return this.comment;}
    public String getEventID() {return this.ID;}
    public Bitmap getImage() {return this.image;}
    public GeoPoint getLocation() {return this.location;}

}
