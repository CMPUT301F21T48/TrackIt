package com.example.trackit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();;
    final CollectionReference collectionReference = db.collection("Cities");
    EditText inputUsername;
    EditText inputPassword;
    Button registerButton;
    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputUsername = findViewById(R.id.register_username);
        inputPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String registerUsername = inputUsername.getText().toString();
                final String registerPassword = inputPassword.getText().toString();

                HashMap<String, String> data = new HashMap<>();
                if (registerUsername.length()>0 && registerPassword.length()>0) {
                    data.put("Password", registerPassword);

                    collectionReference
                            .document(registerUsername)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });

                    inputUsername.setText("");
                    inputPassword.setText("");
                }
            }
        });
    }
}