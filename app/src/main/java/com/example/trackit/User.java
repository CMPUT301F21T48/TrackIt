package com.example.trackit;

import java.io.Serializable;
/**
 * User is the object representing a user
 * Contains setters and getters for the username and password
 * times it was not done.
 */

public class User implements Serializable {
    private String username;
    private String password;

    public User(String Username, String Password) {
        this.username = Username;
        this.password = Password;
    }

    public User() {}

    /**
     * this returns the user name
     * @return return the user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * sets the user name to the registered username
     * @param registeredUsername
     *          registered user name to set
     */
    public void setUsername(String registeredUsername) {
        username = registeredUsername;
    }

    /**
     * returns the password of the user
     * @return
     *      password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * sets the password of the user to the registered password
     * @param registeredPassword
     *      password the user registered with
     */
    public void setPassword(String registeredPassword) {
        password = registeredPassword;
    }


}
