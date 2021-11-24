package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * This is the welcome activity for the HabitUp application.
 * From this activity, the user can go to login or to register
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        Intent intent = new Intent(this, UserSearchActivity.class);
//        startActivity(intent);
        final Button loginButton = findViewById(R.id.login_button);
        final Button registerButton = findViewById(R.id.register_button);
    }

    /**
     * go to Login Activity
     * @param view
     *    instance of object View
     */
    public void goToLogin(View view) {
        // add Intent here
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * got to Register Activity
     * @param view
     *      instance of object View
     */
    public void goToRegister(View view) {
        // add Intent here
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}