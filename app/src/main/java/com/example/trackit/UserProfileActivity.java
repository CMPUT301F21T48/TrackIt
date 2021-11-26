package com.example.trackit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
 * the number of users that follow them
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
    TextView emptyMessage;
    FloatingActionButton addButton;
    Habit habit;
    String followStatus;
    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        emptyMessage = findViewById(R.id.no_habit_message);
        followButton = findViewById(R.id.followButton);
        userNameView = findViewById(R.id.userNameView);
        userFollowers = findViewById(R.id.followerCount);
        userFollowing = findViewById(R.id.followingCount);

        addButton = findViewById(R.id.add_habit);
        habitList = findViewById(R.id.habitList);
        habitDataList = new ArrayList<>();
        habitAdapter = new HabitCustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        String currentUserName = getIntent().getStringExtra("currentUser");
        String chosenUserName = getIntent().getStringExtra("chosenUser");

        Boolean exists = false;

        userNameView.setText(chosenUserName);
        currentUser.setUsername(currentUserName);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, AddHabitActivity.class);
                intent.putExtra("User", currentUser);
                startActivity(intent);
            }
        });

        getDataFromFB(new AsyncCall() {
            @Override
            public void onCallBack(Integer finalCheckValue) {
                followers = finalCheckValue;
                userFollowers.setText("Followers: " + followers);
            }

        }, chosenUserName, "Followers");

        getDataFromFB(new AsyncCall() {
            @Override
            public void onCallBack(Integer finalCheckValue) {
                following = finalCheckValue;
                userFollowing.setText("Following: " + following);
            }
        }, chosenUserName, "Following");



        if(chosenUserName.compareTo(currentUserName) == 0){
            followButton.setText("Your Profile");
            followButton.setEnabled(false);

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
                        String habitPrivacy = (String) doc.getData().get("privacy");

                        int numDone = (int) ((long) doc.getData().get("numDone"));
                        int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                        Habit newHabit = new Habit(title, reason, startDate, repeatDays, habitPrivacy);
                        newHabit.setHabitID(ID);
                        newHabit.setNumDone(numDone);
                        newHabit.setNumNotDone(numNotDone);
                        newHabit.setProgress();
                        habitDataList.add(newHabit);
                    }
                    if (habitDataList.size() == 0) {
                        emptyMessage.setVisibility(View.VISIBLE);
                        emptyMessage.setText("You have not added any habit(s).");
                    }
                    habitAdapter.notifyDataSetChanged();
                };
            });

            habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    habitList.setSelection(position);
                    habit = (Habit) habitList.getItemAtPosition(position);
                    Intent intent = new Intent(UserProfileActivity.this, ViewHabitActivity.class);
                    intent.putExtra("User", currentUser);
                    intent.putExtra("HabitID", habit.getHabitID());
                    intent.putExtra("Habit", habit);
                    startActivity(intent);
                }
            });
        }
        else{
            checkExistence(new ExistsAsyncCall() {
                @Override
                public void onCallBack(Boolean exists) {
                    if(exists) {
                        checkFollow(new FollowExistsAsyncCall() {
                            @Override
                            public void onCallBack(Boolean exists) {
                                if(exists){
                                    followButton.setText("Unfollow");

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
                                                String habitPrivacy = (String) doc.getData().get("privacy");
                                                int numDone = (int) ((long) doc.getData().get("numDone"));
                                                int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                                                Habit newHabit = new Habit(title, reason, startDate, repeatDays, habitPrivacy);
                                                newHabit.setHabitID(ID);
                                                newHabit.setNumDone(numDone);
                                                newHabit.setNumNotDone(numNotDone);
                                                newHabit.setProgress();
                                                if (newHabit.getPrivacy().equals("public")){
                                                    habitDataList.add(newHabit);
                                                }
                                            }
                                            if (habitDataList.size() == 0) {
                                                emptyMessage.setVisibility(View.VISIBLE);
                                                emptyMessage.setText("This user does not have any added habit(s).");
                                            }
                                            habitAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                                else{
                                    followButton.setText("Requested");
                                    emptyMessage.setVisibility(View.VISIBLE);
                                    emptyMessage.setText("You do not have permission to view this user's habits.");
                                }
                            }
                        }, "Followers", chosenUserName, currentUserName);

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
                    }
                    else{
                        followButton.setText("Follow");
                        emptyMessage.setVisibility(View.VISIBLE);
                        emptyMessage.setText("You do not have permission to view this user's habits.");
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

    public interface FollowExistsAsyncCall{
        void onCallBack(Boolean exists);
    }

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

    public void getDataFromFB(AsyncCall asyncCall, String Username, String CollectionList){
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
}
