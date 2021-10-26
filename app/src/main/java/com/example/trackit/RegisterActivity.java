package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText inputUsername;
    EditText inputPassword;
    String registerUsername;
    String registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void signUp (View view) {
        // Retrieve data and register user
        inputUsername = findViewById(R.id.register_username);
        inputPassword = findViewById(R.id.register_password);

        registerUsername = inputUsername.getText().toString();
        registerPassword = inputPassword.getText().toString();
    }
}