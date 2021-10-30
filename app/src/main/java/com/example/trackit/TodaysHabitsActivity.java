package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.Calendar;

public class TodaysHabitsActivity extends AppCompatActivity {

    Intent intent;
    ListView habitList;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    Calendar calendar;
    int day;

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
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        FloatingActionButton addButton = findViewById(R.id.add_button);

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
                    int numDone = (int) ((long) doc.getData().get("numDone"));
                    int numNotDone = (int) ((long) doc.getData().get("numNotDone"));
                    Habit newHabit = new Habit(title, reason, startDate, repeatDays);
                    newHabit.setHabitID(ID);
                    newHabit.setNumDone(numDone);
                    newHabit.setNumNotDone(numNotDone);
                    newHabit.setProgress();
                    if (day == Calendar.MONDAY) {
                        String repeatDay = "M";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            habitDataList.add(newHabit);
                        }
                    }
                    else if (day == Calendar.TUESDAY) {
                        String repeatDay = "T";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
                    }
                    else if (day == Calendar.WEDNESDAY) {
                        String repeatDay = "W";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
                    }
                    else if (day == Calendar.THURSDAY) {
                        String repeatDay = "R";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
                    }
                    else if (day == Calendar.FRIDAY) {
                        String repeatDay = "F";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
                    }
                    else if (day == Calendar.SATURDAY) {
                        String repeatDay = "S";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
                    }
                    else if (day == Calendar.SUNDAY) {
                        String repeatDay = "Su";
                        if (newHabit.getRepeatDays().contains(repeatDay)) {
                            if (!habitDataList.contains(newHabit)) {
                                habitDataList.add(newHabit);
                            }
                        }
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