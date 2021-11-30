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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trackit.Events.AddEventActivity;
import com.example.trackit.Feed.FeedActivity;
import com.example.trackit.Habits.AddHabitActivity;
import com.example.trackit.Habits.Habit;
import com.example.trackit.Habits.HabitCustomList;
import com.example.trackit.Habits.ViewHabitActivity;
import com.example.trackit.Notification.NotificationsActivity;
import com.example.trackit.Search.UserSearchActivity;
import com.example.trackit.User.User;
import com.example.trackit.User.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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
/**
 * This is the activity to display today's habits for the user. It displays all the habits for today
 * and their details. User can click on the habit to view details and edit details. User can also
 * remove the habit from the list. User can select the check mark when they finish today's habit and
 * the progress value of the habit will be increased. The user can also choose to add more habits
 * here if they wish to. Finally, users can use the navigation bar at the bottom to navigate to the
 * different activities of the app such as Search, Notifications, and Profile.
 */
public class TodaysHabitsActivity extends AppCompatActivity {

    final String TAG = "Sample";
    Intent intent;
    ListView habitList;
    BottomNavigationView navBar;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;
    TextView emptyMessage;
    ArrayList<String> finishedHabits;
    User user;
    Habit habit;
    LinearLayout habitMenu;
    boolean[] isClicked = {false};
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    String todayDate;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    Integer currentHabitIndex;
    Integer previousHabitIndex;
    Integer nextHabitIndex;

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
        habitAdapter = new HabitCustomList(this, habitDataList);
        habitList.setAdapter(habitAdapter);

        FloatingActionButton addButton = findViewById(R.id.add_button);

        // Add new habit
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
                    String habitPrivacy = (String) doc.getData().get("privacy");

                    // Filter for today's habits
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
                        Habit newHabit = new Habit(title, reason, startDate, repeatDays, habitPrivacy);
                        newHabit.setHabitID(ID);
                        newHabit.setNumDone(numDone);
                        newHabit.setNumNotDone(numNotDone);
                        newHabit.setProgress();
                        if (!(todayDate.equals(habitLastDone))) {
                            habitDataList.add(newHabit);
                        }
                    }
                }
                // Displays message if there are no habits for today
                if (habitDataList.size() == 0) {
                    emptyMessage.setVisibility(View.VISIBLE);
                } else {
                    emptyMessage.setVisibility(View.GONE);
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

        // Displays/hides the habit menu where users can update progress, reorder habits, or view habit details
        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                habitMenu = view.findViewById(R.id.habit_menu);
                habitList.setSelection(position);
                habit = (Habit) habitList.getItemAtPosition(position);

                if (!isClicked[0]) {
                    habitMenu.setVisibility(View.VISIBLE);
                    isClicked[0] = true;
                    for (int i=0; i<habitList.getCount(); i++){
                        if (i!=position) {
                            arg0.getChildAt(i).findViewById(R.id.habit_menu).setVisibility(GONE);
                        }
                    }
                } else {
                    habitMenu.setVisibility(GONE);
                    isClicked[0] = false;
                    for (int i=0; i<habitList.getCount(); i++){
                        if (i!=position) {
                            arg0.getChildAt(i).findViewById(R.id.habit_menu).setVisibility(GONE);
                        }
                    }
                }
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
//                    Toast.makeText(TodaysHabitsActivity.this, "Coming soon.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TodaysHabitsActivity.this, NotificationsActivity.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                } else if (selectedItem.equals("Feed")) {
                    Intent intent = new Intent(TodaysHabitsActivity.this, FeedActivity.class);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    /**
     * This takes the user to the ViewHabitActivity where they can see the details of the selected
     * habit
     * @param view
     */
    public void viewHabit(View view) {
        intent = new Intent(TodaysHabitsActivity.this, ViewHabitActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("HabitID", habit.getHabitID());
        intent.putExtra("Habit", habit);
        startActivity(intent);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    /**
     * Updates the number of times habit has been done. Also removes habit from today's habits list.
     * Updates "lastDone" value of habit to current date.
     * @param view
     */
    public void habitDone(View view) {
        habit.updateNumDone();
        habit.setLastDone(todayDate);
        collectionReference.document(habit.getHabitID()).set(habit);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
        intent = new Intent(TodaysHabitsActivity.this, AddEventActivity.class);
        intent.putExtra("User", (Serializable) user);
        intent.putExtra("Habit", (Serializable) habit);
        startActivity(intent);
    }

    /**
     * Updates the number of times habit has not been done. Also removes habit from today's habits list.
     * Updates "lastDone" value of habit to current date.
     * @param view
     */
    public void habitNotDone(View view) {
        habit.updateNumNotDone();
        habit.setLastDone(todayDate);
        collectionReference.document(habit.getHabitID()).set(habit);
        habitMenu.setVisibility(GONE);
        isClicked[0] = false;
    }

    /**
     * Moves selected habit up in the list.
     * @param view
     */
    public void moveHabitUp(View view) {
        currentHabitIndex = habitDataList.indexOf(habit);
        if (currentHabitIndex != 0) {
            previousHabitIndex = currentHabitIndex - 1;
            Habit tempHabit = habitDataList.get(previousHabitIndex);
            habitDataList.set(previousHabitIndex, habit);
            habitDataList.set(currentHabitIndex, tempHabit);
            habitAdapter.notifyDataSetChanged();
            habitMenu.setVisibility(GONE);
            isClicked[0] = false;
        }
        // If habit is at the top of the list
        else {
            Snackbar.make(this, view, "Habit cannot be moved up any further.", Snackbar.LENGTH_SHORT).show();
            habitMenu.setVisibility(GONE);
            isClicked[0] = false;
        }
    }

    /**
     * Moves selected habit down in the list.
     * @param view
     */
    public void moveHabitDown(View view) {
        currentHabitIndex = habitDataList.indexOf(habit);
        if (currentHabitIndex != habitDataList.size()-1) {
            nextHabitIndex = currentHabitIndex + 1;
            Habit tempHabit = habitDataList.get(nextHabitIndex);
            habitDataList.set(nextHabitIndex, habit);
            habitDataList.set(currentHabitIndex, tempHabit);
            habitAdapter.notifyDataSetChanged();
            habitMenu.setVisibility(GONE);
            isClicked[0] = false;
        }
        // If habit is at the bottom of the list
        else {
            Snackbar.make(this, view, "Habit cannot be moved down any further.", Snackbar.LENGTH_SHORT).show();
            habitMenu.setVisibility(GONE);
            isClicked[0] = false;
        }
    }

    /**
     * Logs out user from app.
     * @param view
     */
    public void logoutProfile(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(TodaysHabitsActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Overrides onBackPressed method to disable signing out user on clicking back button from
     * TodaysHabitsActivity
     */
    @Override
    public void onBackPressed() {

    }
}