package com.example.trackit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 * This is the activity to add a habit. The user must set a habit name and a descritption for the habit.
 * By default, the start date of the habit is set to the current date.
 * The user may change it to another date.
 * The user must select at least 1 day for the habit schedule.
 * If the user misses a field while adding habit the user is informed to not leave any missing fields
 *
 */
public class AddHabitActivity extends AppCompatActivity {

    EditText addHabitTitle;
    EditText addHabitReason;
    Button addHabitButton;
    Button cancelButton;
    DatePicker datePicker;
    TextView selectedDate;
    String addStartDate;
    CheckBox repeatMonday;
    CheckBox repeatTuesday;
    CheckBox repeatWednesday;
    CheckBox repeatThursday;
    CheckBox repeatFriday;
    CheckBox repeatSaturday;
    CheckBox repeatSunday;
    RadioButton publicPrivacy;
    RadioButton privatePrivacy;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        user = (User) getIntent().getSerializableExtra("User");

        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);

        addHabitButton = findViewById(R.id.button_add_habit);
        cancelButton = findViewById(R.id.button_cancel);
        addHabitReason = findViewById(R.id.add_reason);
        addHabitTitle = findViewById(R.id.add_title);
        repeatMonday = findViewById(R.id.checkbox_monday);
        repeatTuesday = findViewById(R.id.checkbox_tuesday);
        repeatWednesday = findViewById(R.id.checkbox_wednesday);
        repeatThursday = findViewById(R.id.checkbox_thursday);
        repeatFriday = findViewById(R.id.checkbox_friday);
        repeatSaturday = findViewById(R.id.checkbox_saturday);
        repeatSunday = findViewById(R.id.checkbox_sunday);

        publicPrivacy = findViewById(R.id.privacy_button_public);
        privatePrivacy = findViewById(R.id.privacy_button_private);
    }

    /**
     * Uses datePicker to select date and sets that in the textview
     * @param view
     *      instance of object View
     */
    public void selectDate(View view) {
        // Retrieve and set selected start date
        String date = Integer.toString(datePicker.getDayOfMonth());
        if (Integer.valueOf(date) < 10) {
            date = "0" + date;
        }
        String month = Integer.toString(datePicker.getMonth() + 1);
        if (Integer.valueOf(month) < 10) {
            month = "0" + month;
        }
        String year = Integer.toString(datePicker.getYear());
        addStartDate = month + "/" + date + "/" + year;
        selectedDate.setText(addStartDate);
    }

    /**
     * Adds habit to Firestore and to listview
     * @param view
     *      instance of object View
     */
    public void addHabit(View view) {
        String habitTitle = addHabitTitle.getText().toString();
        String habitReason = addHabitReason.getText().toString();
        String habitStartDate = selectedDate.getText().toString();
        ArrayList<String> repeatDays = new ArrayList<>();
        String habitPrivacy = null;

        // Add days of the Habit to the array
        if (repeatMonday.isChecked()){
            repeatDays.add("M");
        }
        if (repeatTuesday.isChecked()){
            repeatDays.add("T");
        }
        if (repeatWednesday.isChecked()){
            repeatDays.add("W");
        }
        if (repeatThursday.isChecked()){
            repeatDays.add("R");
        }
        if (repeatFriday.isChecked()){
            repeatDays.add("F");
        }
        if (repeatSaturday.isChecked()){
            repeatDays.add("S");
        }
        if (repeatSunday.isChecked()){
            repeatDays.add("Su");
        }

        if (publicPrivacy.isChecked()){
            habitPrivacy = "public";
        } else if (privatePrivacy.isChecked()){
            habitPrivacy = "private";
        } else{
            habitPrivacy = "";
        }

        // inform the user to not leave any field empty
        if (habitTitle.isEmpty() || habitReason.isEmpty() || habitStartDate.equals("MM/DD/YYYY") || repeatDays.isEmpty() || habitPrivacy.isEmpty()) {
            Snackbar.make(this, view, "Do not leave any field(s) empty", Snackbar.LENGTH_LONG).show();
        }
        // add the habit to firestore
        else {
            Habit habit = new Habit(habitTitle, habitReason, habitStartDate, repeatDays, habitPrivacy);
            String id = collectionReference.document(user.getUsername()).collection("Habits").document().getId();
            habit.setHabitID(id);
            collectionReference.document(user.getUsername()).collection("Habits").document(id).set(habit);
            finish();
        }
    }

    // cancel option for AddHabit
    public void cancelAddHabit(View view) {
        finish();
    }
}