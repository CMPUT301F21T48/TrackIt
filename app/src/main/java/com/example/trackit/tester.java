package com.example.trackit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class tester extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference collectionReference = db.collection("testpath").document("philza");
    private List<String> Ax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ax = new ArrayList<String>();
        Ax.add("1");
        Ax.add("2");

        collectionReference
                .set(Ax)
                .addOnSuccessListener(unused -> Log.d("sample", "Data has been added successfully!"))
                .addOnFailureListener(e -> Log.d("sample", "Data could not be added!" + e.toString()));



    }
}
