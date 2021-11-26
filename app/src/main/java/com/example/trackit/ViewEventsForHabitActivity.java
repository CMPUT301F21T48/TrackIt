package com.example.trackit;

import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewEventsForHabitActivity extends AppCompatActivity {
    User user;
    Habit habit;
    ListView eventList;
    TextView emptyMessage;
    ArrayAdapter<Event> eventAdapter;
    ArrayList<Event> eventDataList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        eventList = findViewById(R.id.event_list);
        emptyMessage = findViewById(R.id.no_event_message);


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                emptyMessage.setVisibility(View.VISIBLE);
            }
        });
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                eventDataList.clear();
//                if (eventDataList.size()==0) {
//                    emptyMessage.setVisibility(View.VISIBLE);
//                }
//                else{
//                    emptyMessage.setVisibility(View.GONE);
//                }
//            }
//        });
    }

}
