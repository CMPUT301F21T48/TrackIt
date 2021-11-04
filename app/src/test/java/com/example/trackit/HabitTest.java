package com.example.trackit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class HabitTest {
    Habit habit;

    @BeforeEach
    private void mockHabit()
    {
        ArrayList<String> repeatedDays = new ArrayList<String>();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        String title = "Exercise everyday";
        String reason = "To stay healthy";
        String startDate = "02/11/2021";
        habit = new Habit(title,reason, startDate, repeatedDays);
    }

    @Test
    void testHabitName()
    {
        String title = "Exercise everyday";
        assertEquals(habit.getTitle(), title);
        title = "Exercise some days";
        habit.setTitle(title);
        assertEquals(title, habit.getTitle());
    }

    @Test
    void testHabitReason()
    {
        String reason = "To stay healthy";
        assertEquals(habit.getReason(), reason);
        reason = "To stay healthy and live longer";
        habit.setReason(reason);
        assertEquals(reason, habit.getReason());
    }

    @Test
    void testHabitStartDate()
    {
        String startDate = "02/11/2021";
        assertEquals(habit.getStartDate(), startDate);
        startDate = "03/11/2021";
        habit.setStartDate(startDate);
        assertEquals(startDate, habit.getStartDate());
    }

    @Test
    void testHabitRepeatedArray()
    {
        ArrayList<String> repeatedDays = new ArrayList<String>();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
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

        for (int i = 0; i < repeatedDays.size(); i++)
        {

            assertEquals(habit.getRepeatDays().get(i), repeatedDays.get(i));
        }
    }

    @Test
    void testProgressUpdate()
    {
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
