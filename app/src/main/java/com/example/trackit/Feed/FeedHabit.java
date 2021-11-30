package com.example.trackit.Feed;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a feed object to show the habits of other users in feed. This object includes the
 * user name and the corresponding habit title, reason and other relevant details.
 * The only functions it includes are getters and setters.
 */
public class FeedHabit implements Serializable {
    private String title;
    private String reason;
    private ArrayList<String> repeatDays;
    private double progress;
    private String ID;
    private String privacy;
    private int numDone;
    private int numNotDone;
    String username;

    public FeedHabit(String title, String reason, ArrayList<String> repeatDays, String privacy, String username)
    {
        this.title = title;
        this.reason = reason;
        this.repeatDays = repeatDays;
        this.ID = null;
        this.progress = 0;
        this.numDone = 0;
        this.numNotDone = 0;
        this.privacy = privacy;
        this.username = username;
    }

    //getters and setters for the variables
    public void setTitle(String title) {this.title = title;}
    public void setReason(String reason) { this.reason = reason;}
    public void setRepeatDays(ArrayList<String> repeatDays) { this.repeatDays = repeatDays; }
    public void setHabitID(String id) { this.ID = id; }
    public void setNumDone(Integer numDone) {this.numDone = numDone;}
    public void setNumNotDone(Integer numNotDone) { this.numNotDone = numNotDone;}
    public void setProgress() {
        int sum = this.numDone + this.numNotDone;
        if (sum != 0) {
            this.progress = ((double) this.numDone/sum);
        } else { this.progress = 0; }
    }
    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
    public void setUsername(String username) {this.username = username;}

    public String getTitle() { return this.title; }
    public String getReason() { return this.reason; }
    public ArrayList<String> getRepeatDays() { return this.repeatDays; }
    public double getProgress() { return this.progress; }
    public String getHabitID() { return this.ID; }
    public int getNumDone() {return this.numDone;}
    public int getNumNotDone() {return this.numNotDone;}
    public String getPrivacy() {
        return this.privacy;
    }
    public String getUsername() {return this.username;}
}

