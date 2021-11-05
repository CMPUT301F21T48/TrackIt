package com.example.trackit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the activity to display user profile.
 * It displays the users habits, number of individuals they are following and
 * the number of users that follow them.
 */
public class UserProfileActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Users");
    Button followButton;
    TextView userNameView, userFollowers, userFollowing;
    User currentUser = new User("","");
    User chosenUser = new User("","");
    Integer followers = 0;
    Integer following = 0;
    static Integer checkValue = 0;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        followButton = findViewById(R.id.followButton);
        userNameView = findViewById(R.id.userNameView);
        userFollowers = findViewById(R.id.followerCount);
        userFollowing = findViewById(R.id.followingCount);

        habitList = findViewById(R.id.habitList);
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitCustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        String currentUserName = getIntent().getStringExtra("currentUser");
        String chosenUserName = getIntent().getStringExtra("chosenUser");

        Boolean exists = false;


        userNameView.setText(chosenUserName);
        // using onCallBack in order to deal with Async Firebase calls
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



        if(chosenUserName.compareTo(currentUserName) == 0){ //workaround for if the user selects themselves from the user list or just for displaying their profile in general
            followButton.setText("Your Profile");
            followButton.setEnabled(false);
            //displaying the habit of the user
            collectionReference.document(currentUserName).collection("Habits").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    habitDataList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String ID = doc.getId();
                        String title = (String) doc.getData().get("title");
                        String reason = (String) doc.getData().get("reason");
                        String startDate = (String) doc.getData().get("startDate");
                        ArrayList<String> repeatDays = (ArrayList<String>) doc.getData().get("repeatDays");

                        int numDone = (int) ((long) doc.getData().get("numDone"));
                        int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                        Habit newHabit = new Habit(title, reason, startDate, repeatDays);
                        newHabit.setHabitID(ID);
                        newHabit.setNumDone(numDone);
                        newHabit.setNumNotDone(numNotDone);
                        newHabit.setProgress();
                        habitDataList.add(newHabit);
                    }
                    habitAdapter.notifyDataSetChanged();
                };
            });
        }
        else{
            //Using a workaround with onCallBack in order to check if the user exists in the followers/following list
            checkExistence(new ExistsAsyncCall() {
                @Override
                public void onCallBack(Boolean exists) {
                    if(exists) {
                        checkFollow(new FollowExistsAsyncCall() {
                            @Override
                            public void onCallBack(Boolean exists) {
                                if(exists){
                                    followButton.setText("Unfollow");
                                }
                                else{
                                    followButton.setText("Requested");
                                }
                            }
                        }, "Followers", chosenUserName, currentUserName);
                        // in this case it gives the user the option to unfollow by clicking the button
                        followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.collection("Users").document(currentUserName).collection("Following").document(chosenUserName)
                                        .delete();
                                db.collection("Users").document(chosenUserName).collection("Followers").document(currentUserName)
                                        .delete();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            };
                        });
                        // displays the user's habits
                        collectionReference.document(chosenUserName).collection("Habits").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                habitDataList.clear();
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    String ID = doc.getId();
                                    String title = (String) doc.getData().get("title");
                                    String reason = (String) doc.getData().get("reason");
                                    String startDate = (String) doc.getData().get("startDate");
                                    ArrayList<String> repeatDays = (ArrayList<String>) doc.getData().get("repeatDays");

                                    int numDone = (int) ((long) doc.getData().get("numDone"));
                                    int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                                    Habit newHabit = new Habit(title, reason, startDate, repeatDays);
                                    newHabit.setHabitID(ID);
                                    newHabit.setNumDone(numDone);
                                    newHabit.setNumNotDone(numNotDone);
                                    newHabit.setProgress();
                                    habitDataList.add(newHabit);
                                }
                                habitAdapter.notifyDataSetChanged();
                            };
                        });

                    }
                    else{
                        followButton.setText("Follow");
                        followButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, Object> obj =  new HashMap<>();
                                obj.put("Value", "false");
                                db.collection("Users").document(currentUserName).collection("Following").document(chosenUserName)
                                        .set(obj);
                                db.collection("Users").document(chosenUserName).collection("Followers").document(currentUserName)
                                        .set(obj);
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                    }
                }
            },"Followers" , chosenUserName, currentUserName);


        }
    }
// interfaces to work with Firebase
    public interface FollowExistsAsyncCall{
        void onCallBack(Boolean exists);
    }
/**
 * Check follow works with the interface FollowExistsAsynCall in order to work around the fact that Firebase does asynchronous calls, so we
 * can have the information to work with when we need to and doesnt return empty values;
 * checkFollow:
 *    arguments:
 *        followExistsAsynCall : FollowExistsAsyncCall
 *        CollectionList : String
 *        checkingUserName1 : String
 *        checkingUserName2 : String
 * returns:

 **/
    public void checkFollow(FollowExistsAsyncCall followExistsAsyncCall, String CollectionList,String checkingUserName1, String checkingUserName2){
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
                                        if( document.get("Value").toString().compareTo("true") == 0 ) {
                                            exists[0] = true; //this is where i left at for some reason the value doesnt increase outside of this
                                        }
                                    }
                                }
                            }
                            followExistsAsyncCall.onCallBack(exists[0]);
                        }
                    }
                });

    }
    // interfaces to work with Firebase
    public interface ExistsAsyncCall{
        void onCallBack(Boolean exists);
    }

    /**
     * Check existence works with the interface ExistsAsynCall in order to work around the fact that Firebase does asynchronous calls, so we
     * can have the information to work with when we need to and doesnt return empty values;
     * checkExistence:
     *    arguments:
     *        existsAsynCall : ExistsAsyncCall
     *         CollectionList : String
     *         checkingUserName1 : String
     *         checkingUserName2 : String
     *     returns:
     **/

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
// interfaces to work with firebase
    public interface AsyncCall{
        void onCallBack(Integer finalCheckValue);
    }
    /**
     * getDatafromFB works with the interface AsynCall in order to work around the fact that Firebase does asynchronous calls, so we
     * can have the information to work with when we need to and doesnt return empty values;
     * getDatafromFB:
     *    arguments:
     *        asynCall : AsyncCall
     *        CollectionList : String
     *        Username : String
     *    returns:
     **/
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
                                        Value[0]++;
                                    }
                                }
                            }
                            asyncCall.onCallBack(Value[0]);
                        }
                    }
                });
    }

}
