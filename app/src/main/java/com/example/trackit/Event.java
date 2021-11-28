package com.example.trackit;

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
    private Double latitude;
    private Double longitude;
    private String encodedImage;
    private String date;
    private String ID;

    public Event()
    {
        this.comment = null;
        this.encodedImage = null;
        this.date = null;
        this.longitude = null;
        this.latitude = null;
        this.ID = null;
    }

    public Event(String comment, String encodedImage, String date, Double longitude, Double latitude){
        this.comment = comment;
        this.encodedImage= encodedImage;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ID = null;
    }
    //getters and setters for the variables
    public void setComment(String comment) {this.comment = comment;}
    public void setLatitude(Double latitude) {this.latitude = latitude;}
    public void setLongitude(Double longitude) {this.longitude = longitude;}
    public void setImage(String encodedImage) {this.encodedImage = encodedImage;}
    public void setEventID(String ID) {this.ID = ID;}
    public void setEventDate(String date){this.date=date;}

    public String getComment() {return this.comment;}
    public String getEventID() {return this.ID;}
    public String getImage() {return this.encodedImage;}
    public Double getLongitude() {return this.longitude;}
    public Double getLatitude() {return this.latitude;}
    public String getEventDate(){return this.date;}

}
