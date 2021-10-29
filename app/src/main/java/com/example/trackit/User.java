package com.example.trackit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    List<Habit> userHabits;
    static private Dictionary<String, Integer> userFollowers;
    static private Dictionary<String, Integer> userFollowing;

    public User(String Username, String Password) {
        this.userHabits = new ArrayList<Habit>();
        this.username = Username;
        this.password = Password;
        this.userFollowers = new Hashtable<String, Integer>();
        this.userFollowing = new Hashtable<String, Integer>();
    }

    public static Dictionary<String, Integer> getFollower() {
        return userFollowers;
    }

    public static void setFollower(Dictionary<String, Integer> follower) {
        userFollowers = follower;
    }

    public static Dictionary<String, Integer> getFollowing() {
        return userFollowing;
    }

    public static void setFollowing(Dictionary<String, Integer> following) {
        userFollowing = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String registeredUsername) {
        username = registeredUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String registeredPassword) {
        password = registeredPassword;
    }

    public void addHabit(Habit habit) {
        userHabits.add(habit);
    }
}