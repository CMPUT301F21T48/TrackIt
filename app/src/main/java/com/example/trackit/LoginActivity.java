package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This is the the start up launch activity where the user can login into the HabitUp
 * application. If a username does not exist, the user cannot sign in.
 * A username cannot be empty.
 * Once the user logs in, they will be taken to their today's habits page.
 */
public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    EditText inputUsername;
    EditText inputPassword;
    Button loginButton;
    User user = new User("","");
    final String TAG = "Sample";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // started off by initializing authentication

        inputUsername = findViewById(R.id.login_username);
        inputPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login);

        loginButton.setOnClickListener(view ->{
            final String givenUsername = inputUsername.getText().toString();
            final String givenPassword = inputPassword.getText().toString();
            HashMap<String, String> credentials = new HashMap<String, String>();
            String Username = givenUsername + "@example.com";
            if (inputUsername.length()==0){
                inputUsername.requestFocus();
                inputUsername.setError(getString(R.string.error_username_empty));
            }

            // Ensure password is not ""
            else if (inputPassword.length()==0) {
                inputPassword.requestFocus();
                inputPassword.setError("Password field cannot be empty.");
            }
            else {
                mAuth.signInWithEmailAndPassword(Username, givenPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //update here
                                    User mUser = new User("", "");
                                    mUser.setPassword(givenPassword);
                                    mUser.setUsername(givenUsername);
                                    Intent intent = new Intent(LoginActivity.this, TodaysHabitsActivity.class);
                                    intent.putExtra("User", (Serializable) mUser);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Snackbar.make(findViewById(R.id.title_bar), "Incorrect username/password. Please try again.", Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}