package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Button loginButton = findViewById(R.id.login_button);
        final Button registerButton = findViewById(R.id.register_button);
    }

    public void goToLogin(View view) {
        // add Intent here
    }

    public void goToRegister(View view) {
        // add Intent here
    }
}