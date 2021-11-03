package com.example.trackit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class userProfileActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    Button followButton;
    TextView userNameView, userFollowers, userFollowing;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followButton = findViewById(R.id.followButton);
        userNameView = findViewById(R.id.userNameView);
        userFollowers = findViewById(R.id.followerCount);
        userFollowing = findViewById(R.id.followingCount);

        String currentUser = getIntent().getStringExtra("currentUser");
        String chosenUser = getIntent().getStringExtra("chosenUser");

        if(chosenUser == currentUser){

        }

    }
}
