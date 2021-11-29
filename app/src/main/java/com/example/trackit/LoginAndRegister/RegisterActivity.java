package com.example.trackit.LoginAndRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackit.R;
import com.example.trackit.TodaysHabitsActivity;
import com.example.trackit.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashSet;
import java.util.Set;
/**
 * This is the the start up launch activity where the user can register into the HabitUp
 * application. If a username does not exist, the user cannot sign in.
 * A username cannot be empty.
 * Once the user logs in, they will be taken to their today's habits page.
 */
public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    EditText inputUsername;
    EditText inputPassword;
    Button registerButton;

    final String TAG = "Sample";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.register_username);
        inputPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance(); // started off by initializing authentication

        registerButton.setOnClickListener(view -> {
            final String registerUsername = inputUsername.getText().toString();
            final String registerPassword = inputPassword.getText().toString();
            Set<String> names = new HashSet<>();
            if (registerUsername.length() == 0) {
                inputUsername.requestFocus();
                inputUsername.setError(getString(R.string.error_username_empty));
            }

            // Ensure password is not ""
            else if (registerPassword.length() == 0) {
                inputPassword.requestFocus();
                inputPassword.setError("Password field cannot be empty");
            } else {
                String Username = registerUsername + "@example.com";
                mAuth.createUserWithEmailAndPassword(Username , registerPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    User mUser = new User("", "");
                                    mUser.setPassword("Authenticated using Firebase");
                                    mUser.setUsername(registerUsername);
                                    //do update here
                                    collectionReference
                                            .document(registerUsername)
                                            .set(mUser)
                                            .addOnSuccessListener(unused -> Log.d(TAG, "Data has been added successfully!"))
                                            .addOnFailureListener(e -> Log.d(TAG, "Data could not be added!" + e.toString()));
                                    inputPassword.setText("");
                                    inputUsername.setText("");
                                    Intent intent = new Intent(RegisterActivity.this, TodaysHabitsActivity.class);
                                    intent.putExtra("User", mUser);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthWeakPasswordException e) {
                                        String reason = ((FirebaseAuthWeakPasswordException) task.getException()).getReason();
                                        Toast.makeText(RegisterActivity.this, reason,
                                                Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        Toast.makeText(RegisterActivity.this, "Invalid username.",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (FirebaseAuthUserCollisionException e){
                                        Toast.makeText(RegisterActivity.this, "Username already exists.",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        });
    }
}