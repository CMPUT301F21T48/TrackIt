package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public class TodaysHabitsActivity extends AppCompatActivity {

    Intent intent;
    ListView habitList;
    BottomNavigationView navBar;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    TextView emptyMessage;

    CustomList customList;
    User user;
    Habit habit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    final String TAG = "Sample";
    LinearLayout habitMenu;
    final boolean[] isClicked = {false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_habits);

        user = (User) getIntent().getSerializableExtra("User");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        habitList = findViewById(R.id.habit_list);
        navBar = findViewById(R.id.navigation);
        emptyMessage = findViewById(R.id.no_habit_message);
        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

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
                if (habitDataList.size()==0) {
                    emptyMessage.setVisibility(View.VISIBLE);
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
                habitMenu = view.findViewById(R.id.habit_menu);
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
                        habitList.setSelection(position);
                        habit = (Habit) habitList.getItemAtPosition(position);
                    }
                });

            }
        });

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String selectedItem = item.getTitle().toString();
                if (selectedItem.equals("Search")) {
                    Intent intent = new Intent(TodaysHabitsActivity.this, UserSearchActivity.class);
                    startActivity(intent);
                }
                else if (selectedItem.equals("Profile")) {
                    Toast.makeText(TodaysHabitsActivity.this, "Item selected: " + selectedItem, Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(TodaysHabitsActivity.this, UserProfileActivity.class);
//                    intent.putExtra("User", user);
//                    startActivity(intent);
                }
                return false;
            }
        });

    }

    public void viewHabit(View view) {
        habitMenu.setVisibility(View.GONE);
        isClicked[0] = false;
        intent = new Intent(TodaysHabitsActivity.this, ViewHabitActivity.class);
        intent.putExtra("User", (Serializable) user);
        intent.putExtra("HabitID", habit.getHabitID());
        intent.putExtra("Habit", habit);
        startActivity(intent);
    }

    public void habitDone(View view) {
        habitMenu.setVisibility(View.GONE);
        isClicked[0] = false;
        habit.updateNumDone();
        collectionReference.document(habit.getHabitID()).set(habit);
    }

    public void habitNotDone(View view) {
        habitMenu.setVisibility(View.GONE);
        isClicked[0] = false;
        habit.updateNumNotDone();
        collectionReference.document(habit.getHabitID()).set(habit);
    }

    public void logoutProfile(View view) {
        finish();
    }
}