package com.example.trackit;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    ListView feedList;
    ArrayList<User> followingUserList;
    ArrayAdapter<FeedHabit> feedAdapter;
    ArrayList<FeedHabit> feedDataList;
    User user;
    Habit habit;
    TextView emptyMessage;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        user = (User) getIntent().getSerializableExtra("User");

        feedList = findViewById(R.id.habit_list);
        emptyMessage = findViewById(R.id.no_habit_message);

        feedDataList = new ArrayList<>();
        feedAdapter = new FeedHabitCustomList(this, feedDataList);
        feedList.setAdapter(feedAdapter);

        createFeed(user);
    }

    public void createFeed(User user) {
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Following");
        ArrayList<User> returnList = new ArrayList<>();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc : value) {
                    String username = doc.getId();
                    if (doc.getData().get("Value").equals("true")) {
                        returnList.add(new User(username, ""));
                    }
                }

                if (returnList.size() == 0) {
                    emptyMessage.setVisibility(View.VISIBLE);
                    emptyMessage.setText("You are currently not following anyone. Follow users to see their habits here.");
                } else {
                    emptyMessage.setVisibility(View.GONE);
                }
                for (int i=0; i<returnList.size(); i++) {
                    String username = returnList.get(i).getUsername();
                    db.collection("Users").document(username).collection("Habits")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (QueryDocumentSnapshot doc : value) {
                                String habitId = doc.getId();
                                String habitTitle = (String) doc.getData().get("title");
                                String reason = (String) doc.getData().get("reason");
                                ArrayList<String> repeatDays = (ArrayList<String>) doc.getData().get("repeatDays");
                                String habitPrivacy = (String) doc.getData().get("privacy");


                                int flag = 0;
                                String day = LocalDate.now().getDayOfWeek().name();
                                String dayFirstLetter = String.valueOf(day.charAt(0));

                                for (int i = 0; i < repeatDays.size(); i++) {
                                    if (day.startsWith("TH")) {
                                        if (repeatDays.get(i).equals("R")) {
                                            flag = 1;
                                        }
                                    } else {
                                        if (repeatDays.get(i).length() > 1) {
                                            if (day.substring(0, 2).equals(repeatDays.get(i))) {
                                                flag = 1;
                                            }
                                        } else if (dayFirstLetter.equals(repeatDays.get(i))) {
                                            flag = 1;
                                        }
                                    }
                                }

                                if (flag == 1) {
                                    int numDone = (int) ((long) doc.getData().get("numDone"));
                                    int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                                    FeedHabit newHabit = new FeedHabit(habitTitle, reason, repeatDays, habitPrivacy, username);
                                    newHabit.setHabitID(habitId);
                                    newHabit.setNumDone(numDone);
                                    newHabit.setNumNotDone(numNotDone);
                                    newHabit.setProgress();
                                    if (newHabit.getPrivacy().equals("public")) {
                                        feedDataList.add(newHabit);
                                    }
                                }
                            }
                            if (feedDataList.size() == 0) {
                                emptyMessage.setVisibility(View.VISIBLE);
                                emptyMessage.setText("The users you are following do not have any habits listed for today.");
                            } else {
                                emptyMessage.setVisibility(View.GONE);
                            }
                            feedAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }
}