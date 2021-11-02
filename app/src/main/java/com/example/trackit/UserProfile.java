package com.example.trackit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class UserProfile extends AppCompatActivity {

    Intent intent;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    TextView title;

    CustomList customList;
    User user;
    Habit habit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        user = (User) getIntent().getSerializableExtra("User");
        //habit = (Habit) getIntent().getSerializableExtra("Habit");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        title = findViewById(R.id.title_bar);
        title.setText(user.getUsername());

        habitList = findViewById(R.id.habit_list);

        habitAdapter = new CustomList(this, habitDataList);

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //List<Missions> mMissionsList = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Habit habit_ = document.toObject(Habit.class);
                        habitDataList.add(habit_);
                    }
                    habitList.setAdapter(habitAdapter);
                } else {
                    Log.d("HabitActivity", "Error getting documents: ", task.getException());
                }
            }
        });



        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                LinearLayout habitMenu = view.findViewById(R.id.habit_menu);

                final boolean[] isClicked = {false};
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isClicked[0]) {
                            habitMenu.setVisibility(View.VISIBLE);
                            isClicked[0] = true;
                        } else {
                            habitMenu.setVisibility(View.GONE);
                            isClicked[0] = false;
                        }
                    }
                });
                habitList.setSelection(position);
                habit = (Habit) habitList.getItemAtPosition(position);
            }
        });

    }
    public void viewHabit(View view)
    {
        intent = new Intent(UserProfile.this, ViewHabitActivity.class);
        intent.putExtra("User", (Serializable) user);
        intent.putExtra("Habit", (Serializable) habit);
        startActivity(intent);
    }
}
