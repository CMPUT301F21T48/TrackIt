package com.example.trackit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class HabitTest {

    public Habit mockHabit()
    {
        ArrayList<String> repeatedDays = new ArrayList<String>();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        String title = "Exercise everyday";
        String reason = "To stay healthy";
        String startDate = "02/11/2021";
        return new Habit(title,reason, startDate, repeatedDays);
    }
    @Test
    public void testGetSet()
    {
        //getting habit object
        Habit habit = mockHabit();

        //testing the getters
        ArrayList<String> repeatedDays = new ArrayList<String>();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        String title = "Exercise everyday";
        String reason = "To stay healthy";
        String startDate = "02/11/2021";
        assertEquals(habit.getTitle(), title);
        assertEquals(habit.getReason(), reason);
        assertEquals(habit.getStartDate(), startDate);
        for (int i = 0; i < repeatedDays.size(); i++)
        {
            assertEquals(habit.getRepeatDays().get(i), repeatedDays.get(i));
        }

        //testing the setters
        repeatedDays.clear();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        repeatedDays.add("R");
        title = "Exercise some days";
        reason = "To stay healthy and live longer";
        startDate = "03/11/2021";
        habit.setTitle(title);
        assertEquals(title, habit.getTitle());
        habit.setReason(reason);
        assertEquals(reason, habit.getReason());
        habit.setStartDate(startDate);
        assertEquals(startDate, habit.getStartDate());
        for (int i = 0; i < repeatedDays.size(); i++)
        {
            assertEquals(habit.getRepeatDays().get(i), repeatedDays.get(i));
        }
    }

    @Test
    void testProgressUpdate()
    {
        Habit habit = mockHabit();
        assertEquals(0, habit.getProgress());
        habit.updateNumDone();
        assertEquals(1.0, habit.getProgress());
        habit.updateNumNotDone();
        assertEquals(0.5, habit.getProgress());
        habit.updateNumDone();
        habit.updateNumDone();
        assertEquals(0.75, habit.getProgress());

    }
}
