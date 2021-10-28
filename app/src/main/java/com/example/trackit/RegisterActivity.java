package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    EditText inputUsername;
    EditText inputPassword;
    Button registerButton;
    final String TAG = "Sample";

    private void verifyInput(String registerUsername, String registerPassword, Set<String> names){

        HashMap<String, String> data = new HashMap<>();

        if (registerUsername.length()>0 && registerPassword.length()>0) {

            // Ensure we have a unique name
            if(names.contains(registerUsername)){
                inputUsername.requestFocus();
                inputUsername.setError("Name is not unique");
            }

            // Add data to file
            else {
                data.put("Password", registerPassword);

                collectionReference
                        .document(registerUsername)
                        .set(data)
                        .addOnSuccessListener(unused -> Log.d(TAG, "Data has been added successfully!"))
                        .addOnFailureListener(e -> Log.d(TAG, "Data could not be added!" + e.toString()));

                inputUsername.setText("");
                inputPassword.setText("");
            }
        }

        // Ensure name is not ""
        else if (registerUsername.length()==0){
            inputUsername.requestFocus();
            inputUsername.setError("Name cannot be null");
        }

        // Ensure password is not ""
        else{
            inputPassword.requestFocus();
            inputPassword.setError("Password cannot be null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.register_username);
        inputPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(view -> {
            final String registerUsername = inputUsername.getText().toString();
            final String registerPassword = inputPassword.getText().toString();
            Set<String> names = new HashSet<>();


            collectionReference
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                names.add(document.getId());
                            }
                            verifyInput(registerUsername, registerPassword, names);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        });
    }
}