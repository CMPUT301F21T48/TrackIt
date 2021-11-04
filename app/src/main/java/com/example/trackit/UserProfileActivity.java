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
import com.google.android.gms.tasks.OnSuccessListener;
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
    Integer followers = 0;
    Integer following = 0;
    static Integer checkValue = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followButton = findViewById(R.id.followButton);
        userNameView = findViewById(R.id.userNameView);
        userFollowers = findViewById(R.id.followerCount);
        userFollowing = findViewById(R.id.followingCount);

        String currentUserName = "amir";
        String chosenUserName = getIntent().getStringExtra("chosenUser");

        Boolean exists = false;


        userNameView.setText(chosenUserName);

        getDatafromFB(new AsyncCall() {
            @Override
            public void onCallBack(Integer finalCheckValue) {
                followers = finalCheckValue;
                userFollowers.setText("Followers: " + followers);
            }

        }, chosenUserName, "Followers");

        getDatafromFB(new AsyncCall() {
            @Override
            public void onCallBack(Integer finalCheckValue) {
                following = finalCheckValue;
                userFollowing.setText("Following: " + following);
            }
        }, chosenUserName, "Following");



        if(chosenUserName.compareTo(currentUserName) == 0){
            followButton.setText("Your Profile");
            followButton.setEnabled(false);
        }
        else{
            checkExistence(new ExistsAsyncCall() {
                @Override
                public void onCallBack(Boolean exists) {
                    if(exists) {
                        followButton.setText("Unfollow");
                    }
                    else{
                        followButton.setText("Follow");
                    }
                }
            },"Following" , chosenUserName, currentUserName);


        }
    }

    public interface ExistsAsyncCall{
        void onCallBack(Boolean exists);
    }

    public void checkExistence(ExistsAsyncCall existsAsyncCall, String CollectionList,String checkingUserName1, String checkingUserName2){
        final Boolean[] exists = {false};
        db.collection("Users").document(checkingUserName1).collection(CollectionList)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    Log.d("look",document.getId());
                                    if ( document.getId().toString().compareTo(checkingUserName2) == 0) {

                                        exists[0] = true; //this is where i left at for some reason the value doesnt increase outside of this

                                    }
                                }
                            }
                            existsAsyncCall.onCallBack(exists[0]);
                        }
                    }
                });

    }

    public interface AsyncCall{
        void onCallBack(Integer finalCheckValue);
    }

    public void getDatafromFB(AsyncCall asyncCall, String Username, String CollectionList){
        final Integer[] Value = {0};
        db.collection("Users").document(Username).collection(CollectionList)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    Log.d("look", (String) document.get("Value"));
                                    if ( document.get("Value").toString().compareTo("true") == 0) {
                                        Value[0]++; //this is where i left at for some reason the value doesnt increase outside of this

                                    }
                                }
                            }
                            asyncCall.onCallBack(Value[0]);
                        }
                    }
                });
    }

//    public Boolean existsInFB(String )
}
