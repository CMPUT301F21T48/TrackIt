package com.example.trackit;

import java.io.Serializable;
import java.lang.*;
import java.util.*;

public class Habit implements Serializable {
    private String title;
    private String reason;
    private String startDate;
    private ArrayList<String> repeatDays;
    private float progress;
    private int numDone;
    private int numNotDone;
    private String ID;

    public Habit(String title, String reason, String startDate, ArrayList<String> repeatDays)
    {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.repeatDays = repeatDays;
        this.ID = null;
        this.progress = 0;
        this.numDone = 0;
        this.numNotDone = 0;
    }

    //getters and setters for the variables
    public void setTitle(String title) {this.title = title;}
    public void setReason(String reason) { this.reason = reason;}
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setRepeatDays(ArrayList<String> repeatDays) { this.repeatDays = repeatDays; }
    public void setHabitID(String id) {
        this.ID = id;
    }
    public void setNumDone(Integer numDone) {this.numDone = numDone;}
    public void setNumNotDone(Integer numNotDone) { this.numNotDone = numNotDone;}
    public void setProgress() {
        int sum = this.numDone + this.numNotDone;
        if (sum != 0) {
            this.progress = (this.numDone/sum) * 100;
        } else { this.progress = 0; }
    }

    public String getTitle() { return this.title; }
    public String getReason() { return this.reason; }
    public String getStartDate() { return this.startDate; }
    public ArrayList<String> getRepeatDays() { return this.repeatDays; }
    public float getProgress() { return this.progress; }
    public String getHabitID() { return this.ID; }
    public int getNumDone() {return this.numDone;}
    public int getNumNotDone() {return this.numNotDone;}

    public void updateNumDone() { this.numDone++; }
    public void updateNumNotDone() { this.numNotDone++; }
}
