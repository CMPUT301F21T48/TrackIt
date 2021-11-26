package com.example.trackit;

import java.io.Serializable;
import java.lang.*;
import java.util.*;
/**
 * Habit is the object representing a habit type, belonging to a specific user.
 * It consists of a name, reason, schedule of days of the week it should be repeated, the date to start, and
 * a progress bar which will increase each time the habit is done.
 * The Habit performs some calculations for how many times it was done on schedule, and how many
 * times it was not done.
 */

public class Habit implements Serializable {
    private String title;
    private String reason;
    private String startDate;
    private ArrayList<String> repeatDays;
    private double progress;
    private int numDone;
    private int numNotDone;
    private String ID;
    private String lastDone;
    private String privacy;

    public Habit(String title, String reason, String startDate, ArrayList<String> repeatDays, String privacy)
    {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.repeatDays = repeatDays;
        this.lastDone = null;
        this.ID = null;
        this.progress = 0;
        this.numDone = 0;
        this.numNotDone = 0;
        this.privacy = privacy;
    }

    //getters and setters for the variables
    public void setTitle(String title) {this.title = title;}
    public void setReason(String reason) { this.reason = reason;}
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setRepeatDays(ArrayList<String> repeatDays) { this.repeatDays = repeatDays; }
    public void setHabitID(String id) { this.ID = id; }
    public void setNumDone(Integer numDone) {this.numDone = numDone;}
    public void setNumNotDone(Integer numNotDone) { this.numNotDone = numNotDone;}
    public void setLastDone(String lastDone) {
        this.lastDone = lastDone;
    }
    public void setProgress() {
        int sum = this.numDone + this.numNotDone;
        if (sum != 0) {
            this.progress = ((double) this.numDone/sum);
        } else { this.progress = 0; }
    }
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getTitle() { return this.title; }
    public String getReason() { return this.reason; }
    public String getStartDate() { return this.startDate; }
    public ArrayList<String> getRepeatDays() { return this.repeatDays; }
    public double getProgress() { return this.progress; }
    public String getHabitID() { return this.ID; }
    public int getNumDone() {return this.numDone;}
    public int getNumNotDone() {return this.numNotDone;}
    public String getLastDone() {
        return this.lastDone;
    }
    public String getPrivacy() {
        return this.privacy;
    }

    public void updateNumDone() {
        this.numDone++;
        setProgress();
    }
    public void updateNumNotDone() {
        this.numNotDone++;
        setProgress();
    }
}
