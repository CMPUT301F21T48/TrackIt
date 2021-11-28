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
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
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