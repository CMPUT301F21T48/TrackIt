package com.example.trackit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    EditText inputUsername;
    EditText inputPassword;
    Button loginButton;
    User user = new User("","");
    final String TAG = "Sample";

    private void verifyCredentials(String loginUsername, String loginPassword, HashMap<String, String>credentials){

        if (loginUsername.length()>0 && loginPassword.length()>0) {
            if (credentials.containsKey(loginUsername)){
                Log.d(TAG, loginUsername + " => " + credentials.get(loginUsername));
                if (credentials.get(loginUsername).equals(loginPassword)){

                    user.setPassword(loginPassword);
                    user.setUsername(loginUsername);
                    
                    inputUsername.setText("");
                    inputPassword.setText("");
                    Intent intent = new Intent(this, TodaysHabitsActivity.class);
                    intent.putExtra("User", (Serializable) user);
                    startActivity(intent);
                }
                else{
                    inputPassword.requestFocus();
                    inputUsername.setError("Invalid Password");
                }
            }
            else{
                inputUsername.requestFocus();
                inputUsername.setError("Username not registered");
            }
        }

        // Ensure name is not ""
        if (loginUsername.length()==0){
            inputUsername.requestFocus();
            inputUsername.setError(getString(R.string.error_username_empty));
        }

        // Ensure password is not ""
        else{
            inputPassword.requestFocus();
            inputPassword.setError("Password field cannot be empty");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = findViewById(R.id.login_username);
        inputPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(view ->{
            final String givenUsername = inputUsername.getText().toString();
            final String givenPassword = inputPassword.getText().toString();
            HashMap<String, String> credentials = new HashMap<String, String>();

            collectionReference
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                credentials.put(document.getId(),(String) document.get("password"));
                            }
                            verifyCredentials(givenUsername, givenPassword, credentials);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        });
    }
}