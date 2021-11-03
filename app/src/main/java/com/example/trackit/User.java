package com.example.trackit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;

    public User(String Username, String Password) {
        this.username = Username;
        this.password = Password;
    }

    public User() {}

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


}
