package com.example.trackit;

import java.lang.*;
import java.util.*;

public class Habit {
    private String title;
    private String reason;
    private String startDate;
    private ArrayList<String> repeatDays;
    private int progress;
    private int numDone;
    private int numNotDone;

    public Habit(String title, String reason, String startDate, ArrayList<String> repeatDays)
    {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.repeatDays = repeatDays;
    }

    //getters and setters for the variables
    public void setTitle(String title) {this.title = title;}
    public void aetReason() { this.reason = reason;}
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setRepeatDays(ArrayList<String> repeatDays) { this.repeatDays = repeatDays; }

    public void setProgress(int progress) { this.progress = progress; }

    public String getTitle() { return title; }
    public String getReason() { return reason; }
    public String getStartDate() { return startDate; }
    public ArrayList<String> getRepeatDays() { return repeatDays; }
    public int getProgress() { return progress; }

    /**
     *  Returns the progress towards habit
     * @param
     *      isDone 0 if the habit was not completed 1 otherwise
     * @return
     *      the progress as a percentage
     **/
    public float updateProgress(int isDone)
    {
        if (isDone == 0)
            this.numNotDone++;
        else
            this.numDone++;
        setProgress((this.numDone/(this.numDone+this.numNotDone))*100);
        return getProgress();
    }
}
