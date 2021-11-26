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
    private String encodedImage;
    private Bitmap image;
    private String date;
    private String ID;



    public Event()
    {
        this.comment = null;
        this.encodedImage = null;
        this.image = null;
        this.date = null;
        this.location = null;
        this.ID = null;
    }

    //getters and setters for the variables
    public void setComment(String comment) {this.comment = comment;}
    public void setLocation(GeoPoint location) {this.location = location;}
    public void setImage(String encodedImage) {this.encodedImage = encodedImage;}
    public void setEventID(String ID) {this.ID = ID;}
    public void setEventDate(String date){this.date=date;}

    public String getComment() {return this.comment;}
    public String getEventID() {return this.ID;}
    public String getImage() {return this.encodedImage;}
    public GeoPoint getLocation() {return this.location;}
    public String getEventDate(){return this.date;}

}
