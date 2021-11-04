package com.example.trackit;

import static android.view.View.GONE;

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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class TodaysHabitsActivity extends AppCompatActivity {

    final String TAG = "Sample";
    Intent intent;
    ListView habitList;
    BottomNavigationView navBar;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    TextView emptyMessage;
    ArrayList<String> finishedHabits;
    CustomList customList;
    User user;
    Habit habit;
    LinearLayout habitMenu;
    boolean[] isClicked = {false};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    String todayDate;
    Calendar calendar;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_habits);

        user = (User) getIntent().getSerializableExtra("User");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        todayDate = dateFormat.format(calendar.getTime());

        habitList = findViewById(R.id.habit_list);
        navBar = findViewById(R.id.navigation);
        emptyMessage = findViewById(R.id.no_habit_message);
        finishedHabits = new ArrayList<>();

        habitDataList = new ArrayList<>();
        habitAdapter = new CustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        FloatingActionButton addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(TodaysHabitsActivity.this, AddHabitActivity.class);
                intent.putExtra("User", (Serializable) user);
                startActivity(intent);
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String ID = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    String startDate = (String) doc.getData().get("startDate");
                    ArrayList<String> repeatDays = (ArrayList<String>) doc.getData().get("repeatDays");
                    String habitLastDone = (String) doc.getData().get("lastDone");

                    int flag = 0;
                    String day = LocalDate.now().getDayOfWeek().name();
                    String dayFirstLetter = String.valueOf(day.charAt(0));
                    for (int i = 0; i < repeatDays.size(); i++) {
                        if (repeatDays.get(i).length() > 1) {
                            if (day.substring(0, 2).equals(repeatDays.get(i))) {
                                flag = 1;
                            }
                        } else if (dayFirstLetter.equals(repeatDays.get(i))) {
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
                        if (!(todayDate.equals(habitLastDone))) {
                            habitDataList.add(newHabit);
                        }
                    }
                }
                if (habitDataList.size() == 0) {
                    emptyMessage.setVisibility(View.VISIBLE);
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

        //my edits to be removed before commit
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                habitMenu = view.findViewById(R.id.habit_menu);

                if (!isClicked[0]) {
                    habitMenu.setVisibility(View.VISIBLE);
                    isClicked[0] = true;
                } else {
                    habitMenu.setVisibility(GONE);
                    isClicked[0] = false;
                }
                habitList.setSelection(position);
                habit = (Habit) habitList.getItemAtPosition(position);
            }
        });

        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String selectedItem = item.getTitle().toString();
                if (selectedItem.equals("Search")) {
                    Intent intent = new Intent(TodaysHabitsActivity.this, UserSearchActivity.class);
                    intent.putExtra("currentUser", user);
                    startActivity(intent);
                } else if (selectedItem.equals("Profile")) {
//                    Toast.makeText(TodaysHabitsActivity.this, "Item selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TodaysHabitsActivity.this, UserProfileActivity.class);
                    intent.putExtra("currentUser", user.getUsername());
                    intent.putExtra("chosenUser", user.getUsername());
                    startActivity(intent);
                } else if (selectedItem.equals("Notifications")) {
                    Toast.makeText(TodaysHabitsActivity.this, "Coming soon.", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }

    public void viewHabit(View view) {
        intent = new Intent(TodaysHabitsActivity.this, ViewHabitActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("HabitID", habit.getHabitID());
        intent.putExtra("Habit", habit);
        startActivity(intent);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    public void habitDone(View view) {
        habit.updateNumDone();
        habit.setLastDone(todayDate);
        collectionReference.document(habit.getHabitID()).set(habit);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    public void habitNotDone(View view) {
        habit.updateNumNotDone();
        habit.setLastDone(todayDate);
        collectionReference.document(habit.getHabitID()).set(habit);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    public void logoutProfile(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {

    }
}