package com.example.trackit;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
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
    BottomNavigationView navBar;
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
                    String date = (String) doc.getData().get("eventDate");
                    String comment = (String) doc.getData().get("comment");
                    String image = (String) doc.getData().get("image");
                    Double longitude = (Double) doc.getData().get("longitude");
                    Double latitude = (Double) doc.getData().get("latitude");
                    //System.out.println(comment);
                    Event newEvent =new Event(comment, image, date, longitude, latitude);
                    newEvent.setEventID(eventID);
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
