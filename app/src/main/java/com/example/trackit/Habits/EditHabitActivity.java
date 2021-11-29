package com.example.trackit.Habits;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trackit.R;
import com.example.trackit.User.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This is the activity for editing a habit.
 * The user can change the start date and the associated attribute.
 * The user can add or remove more days for the habit's schedule, but must have at least 1 day selected.
 * If the user leaves a field empty while editing a habit the user is informed to not leave any missing fields
 */
public class EditHabitActivity extends AppCompatActivity {

    EditText editHabitTitle;
    EditText editHabitReason;
    Button editHabitButton;
    Button cancelButton;
    String habitID;
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
    RadioGroup privacyGroup;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("Users");
    User user;
    Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        habitID = habit.getHabitID();
        setContentView(R.layout.activity_edit_habit);
        selectedDate = findViewById(R.id.add_start_date_text);
        datePicker = findViewById(R.id.select_start_date);
        editHabitButton = findViewById(R.id.button_edit_habit);
        cancelButton = findViewById(R.id.button_cancel);
        editHabitReason = findViewById(R.id.add_reason);
        editHabitTitle = findViewById(R.id.add_title);
        repeatMonday = findViewById(R.id.checkbox_monday);
        repeatTuesday = findViewById(R.id.checkbox_tuesday);
        repeatWednesday = findViewById(R.id.checkbox_wednesday);
        repeatThursday = findViewById(R.id.checkbox_thursday);
        repeatFriday = findViewById(R.id.checkbox_friday);
        repeatSaturday = findViewById(R.id.checkbox_saturday);
        repeatSunday = findViewById(R.id.checkbox_sunday);
        publicPrivacy = findViewById(R.id.privacy_button_public);
        privatePrivacy = findViewById(R.id.privacy_button_private);
        privacyGroup = findViewById(R.id.habit_privacy_group);

        //adding the previous information for habit in the fields
        editHabitTitle.setText(habit.getTitle());
        editHabitReason.setText(habit.getReason());
        selectedDate.setText(habit.getStartDate());
        if (habit.getPrivacy().equals("public")){
            privacyGroup.check(R.id.privacy_button_public);
        } else{
            privacyGroup.check(R.id.privacy_button_private);
        }

        ArrayList<String> repeatDays = habit.getRepeatDays();
        for (int i = 0; i < repeatDays.size(); i++)
        {
            if (repeatDays.get(i).equals("M"))
                repeatMonday.setChecked(true);
            else if (repeatDays.get(i).equals("T"))
                repeatTuesday.setChecked(true);
            else if (repeatDays.get(i).equals("W"))
                repeatWednesday.setChecked(true);
            else if (repeatDays.get(i).equals("R"))
                repeatThursday.setChecked(true);
            else if (repeatDays.get(i).equals("F"))
                repeatFriday.setChecked(true);
            else if (repeatDays.get(i).equals("S"))
                repeatSaturday.setChecked(true);
            else if (repeatDays.get(i).equals("Su"))
                repeatSunday.setChecked(true);
        }
    }

    /**
     *  Selects the date using datePicker and sets the textview to selected date
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
     *  Save the changes for the habit 
     * @param view
     */
    public void saveChanges(View view) {
        String habitTitle = editHabitTitle.getText().toString();
        String habitReason = editHabitReason.getText().toString();
        String habitStartDate = selectedDate.getText().toString();
        ArrayList<String> repeatDays = new ArrayList<>();
        String habitPrivacy = null;

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
        if (habitTitle.isEmpty() || habitReason.isEmpty() || habitStartDate.equals("MM/DD/YYYY") || repeatDays.isEmpty()) {
            Snackbar.make(this, view, "Do not leave any field(s) empty", Snackbar.LENGTH_LONG).show();
        }
        // add the habit to firestore
        else {
            habit.setTitle(habitTitle);
            habit.setReason(habitReason);
            habit.setStartDate(habitStartDate);
            habit.setRepeatDays(repeatDays);
            habit.setPrivacy(habitPrivacy);
            collectionReference.document(user.getUsername()).collection("Habits").document(habitID).set(habit);
            finish();
        }
    }

    /**
     *  Cancel the add habit
     * @param view
     */
    public void cancelAddHabit(View view) { finish(); }
}