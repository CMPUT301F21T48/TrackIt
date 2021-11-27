package com.example.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
/**
 * This is the activity for viewing a habit.
 * The user can also edit or delete a habit when viewing its details
 */
public class ViewHabitActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;
    User user;
    Habit habit;
    String habitID;
    Intent intent;
    TextView habitTitle;
    TextView habitReason;
    TextView startedDate;
    TextView repeatDays;
    Button edit;
    Button delete;
    final String TAG = "Sample";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit);
        edit = findViewById(R.id.button_edit_habit);
        delete = findViewById(R.id.button_delete_habit);

        habitTitle = findViewById(R.id.add_title);
        habitReason = findViewById(R.id.add_reason);
        startedDate = findViewById(R.id.add_start_date_text);
        repeatDays = findViewById(R.id.add_repeat_days);

        user = (User) getIntent().getSerializableExtra("User");
        habit = (Habit) getIntent().getSerializableExtra("Habit");
        habitID = getIntent().getStringExtra("HabitID");
        collectionReference = db.collection("Users").document(user.getUsername()).collection("Habits");

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        startedDate.setText(habit.getStartDate());
        String textRepeat = "";
        String day;
        for (int i = 0; i < habit.getRepeatDays().size(); i++)
        {
            day = habit.getRepeatDays().get(i);
            if (day.equals("M"))
                day = "Monday\n\n";
            else if (day.equals("T"))
                day = "Tuesday\n\n";
            else if (day.equals("W"))
                day = "Wednesday\n\n";
            else if (day.equals("R"))
                day = "Thursday\n\n";
            else if (day.equals("F"))
                day = "Friday\n\n";
            else if (day.equals("S"))
                day = "Saturday\n\n";
            else
                day = "Sunday\n\n";

            textRepeat += day;
        }
        repeatDays.setText(textRepeat);
    }

    /**
     *  User can edit Habit details when viewing it
     *  Starts intent to call EditHabitActivity
     * @param view
     *      instance of object View
     */
    public void editHabit(View view)
    {
        intent = new Intent(ViewHabitActivity.this, EditHabitActivity.class);
        intent.putExtra("User", (Serializable) user);
        intent.putExtra("Habit", (Serializable) habit);
        startActivity(intent);
        finish();
    }

    /**
     * User can delete a habit when viewing its details
     * @param view
     *      instance of object View
     */
    public void deleteHabit(View view)
    {
        collectionReference.document(habit.getHabitID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        finish();
    }

    public void viewEvents(View view) {

        Intent newIntent = new Intent(this, ViewEventsForHabitActivity.class);
        newIntent.putExtra("User", (Serializable) user);
        newIntent.putExtra("Habit", (Serializable) habit);
        startActivity(newIntent);

    }
}
