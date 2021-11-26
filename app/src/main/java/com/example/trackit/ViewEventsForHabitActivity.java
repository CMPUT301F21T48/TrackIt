package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewEventsForHabitActivity extends AppCompatActivity {
    User user;
    Habit habit;
    ListView eventList;
    TextView emptyMessage;
    TextView habitTitle;
    ArrayAdapter<Event> eventAdapter;
    ArrayList<Event> eventDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    final String TAG = "Sample";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits").document(habit.getHabitID()).collection("Events");

        eventList = findViewById(R.id.event_list);
        emptyMessage = findViewById(R.id.no_event_message);
        habitTitle = findViewById(R.id.habit_title_text);
        habitTitle.setText(habit.getTitle());

        eventDataList = new ArrayList<>();
        eventAdapter = new EventCustomList(this, eventDataList);
        eventList.setAdapter(eventAdapter);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                eventDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String eventID = doc.getId();
                    String date = (String) doc.getData().get("date");
                    String comment = (String) doc.getData().get("comment");
                    //System.out.println(comment);
                    Event newEvent =new Event();
                    newEvent.setEventID(eventID);
                    newEvent.setComment(comment);
                    newEvent.setLocation(null);
                    newEvent.setImage(null);
                    newEvent.setEventDate(date);
                    eventDataList.add(newEvent);
                }
                if (eventDataList.size()==0){
                    emptyMessage.setVisibility(View.VISIBLE);
                } else{
                    emptyMessage.setVisibility(View.GONE);
                }
                eventAdapter.notifyDataSetChanged();



            }

        });


    }

}
