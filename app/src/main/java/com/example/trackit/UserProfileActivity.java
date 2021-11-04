package com.example.trackit;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.atomic.AtomicReference;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    Button followButton;
    TextView userNameView, userFollowers, userFollowing;
    User currentUser = new User("","");
    User chosenUser = new User("","");

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followButton = findViewById(R.id.followButton);
        userNameView = findViewById(R.id.userNameView);
        userFollowers = findViewById(R.id.followerCount);
        userFollowing = findViewById(R.id.followingCount);
        AtomicReference<String> Username = new AtomicReference<>("test");
        AtomicReference<String> Password = new AtomicReference<>("test");

        String currentUserName = "amir";
        String chosenUserName = getIntent().getStringExtra("chosenUser");

        Log.d("msg", "this is what you wanted " + Username + " => ");
        Toast toast = Toast.makeText(this, chosenUser.getUsername(),Toast.LENGTH_SHORT);
        toast.show();

        userNameView.setText(currentUser.getPassword());


        if(chosenUserName.compareTo(currentUserName) == 0){
            followButton.setText("Your Profile");
            followButton.setEnabled(false);
            db.collection("Users").document(chosenUserName).collection("Followers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().size() > 0) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Log.d("msg", "Room already exists, start the chat");

                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "NO FOLLOWERS", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                        }
                    });

        }
        else{

        }

    }
}
