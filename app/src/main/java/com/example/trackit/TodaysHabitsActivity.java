package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;

public class TodaysHabitsActivity extends AppCompatActivity {

    Intent intent;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    CustomList customList;
    User user;
    Habit habit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_habits);

        user = (User) getIntent().getSerializableExtra("User");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        habitList = findViewById(R.id.habit_list);
        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        FloatingActionButton addButton = findViewById(R.id.add_button);
        Button userButton = findViewById(R.id.user);

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(TodaysHabitsActivity.this, AddHabitActivity.class);
                intent.putExtra("User", (Serializable) user);
                startActivity(intent);
            }
        });


        userButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(TodaysHabitsActivity.this, userProfileActivity.class);
                intent.putExtra("User", (Serializable) user);
                startActivity(intent);
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String ID = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    String startDate = (String) doc.getData().get("startDate");
                    ArrayList<String> repeatDays = (ArrayList<String>) doc.getData().get("repeatDays");

                    int flag = 0;
                    String day = LocalDate.now().getDayOfWeek().name();
                    String dayFirstLetter = String.valueOf(day.charAt(0));
                    for (int i = 0; i < repeatDays.size(); i++){
                        if (repeatDays.get(i).length() > 1) {
                            if (day.substring(0,2).equals(repeatDays.get(i))) {
                                flag = 1;
                            }
                        }
                        else if (dayFirstLetter.equals(repeatDays.get(i))) {
                            flag = 1;
                        }
                    }

                    if (flag == 1) {
                        int numDone = (int) ((long) doc.getData().get("numDone"));
                        int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                        Habit newHabit = new Habit(title, reason, startDate, repeatDays);
                        newHabit.setHabitID(ID);
                        newHabit.setNumDone(numDone);
                        newHabit.setNumNotDone(numNotDone);
                        newHabit.setProgress();
                        habitDataList.add(newHabit);
                    }

                }
                habitAdapter.notifyDataSetChanged();
            }
        });

        //my edits to be removed before commit
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
        intent = new Intent(TodaysHabitsActivity.this, ViewHabitActivity.class);
        intent.putExtra("User", (Serializable) user);
        intent.putExtra("Habit", (Serializable) habit);
        startActivity(intent);
    }
}