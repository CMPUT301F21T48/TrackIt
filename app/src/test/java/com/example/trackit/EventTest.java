package com.example.trackit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class EventTest{
    Event event;

    @BeforeEach
    private void mockEvent()
    {
//        ArrayList<String> repeatedDays = new ArrayList();
//        repeatedDays.add("M");
//        repeatedDays.add("T");
//        repeatedDays.add("W");
//        String title = "Exercise everyday";
//        String reason = "To stay healthy";
//        String startDate = "02/11/2021";
//        habit = new Habit(title,reason, startDate, repeatedDays);
        String comment = "";
        event = new Event();

    }

    @Test
    void testHabitName()
    {
        //testing getter for habit name
        String title = "Exercise everyday";
        assertEquals(habit.getTitle(), title);
        //testing setter for habit name
        title = "Exercise some days";
        habit.setTitle(title);
        assertEquals(title, habit.getTitle());
    }

    @Test
    void testHabitReason()
    {
        //testing getter for habit reason
        String reason = "To stay healthy";
        assertEquals(habit.getReason(), reason);
        //testing setter for habit reason
        reason = "To stay healthy and live longer";
        habit.setReason(reason);
        assertEquals(reason, habit.getReason());
    }

    @Test
    void testHabitStartDate()
    {
        //testing getter for habit start date
        String startDate = "02/11/2021";
        assertEquals(habit.getStartDate(), startDate);
        //testing setter for habit start date
        startDate = "03/11/2021";
        habit.setStartDate(startDate);
        assertEquals(startDate, habit.getStartDate());
    }

    @Test
    void testHabitRepeatedArray()
    {
        //testting getter for habit repeated days array
        ArrayList<String> repeatedDays = new ArrayList();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        for (int i = 0; i < repeatedDays.size(); i++)
        {
            assertEquals(habit.getRepeatDays().get(i), repeatedDays.get(i));
        }

        //testing the setter for habit repeated days array
        repeatedDays.clear();
        repeatedDays.add("M");
        repeatedDays.add("T");
        repeatedDays.add("W");
        repeatedDays.add("R");
        habit.setRepeatDays(repeatedDays);
        for (int i = 0; i < repeatedDays.size(); i++)
        {

            assertEquals(habit.getRepeatDays().get(i), repeatedDays.get(i));
        }
    }

    @Test
    void testProgressUpdate()
    {
        //testing initial value for progress
        assertEquals(0, habit.getProgress());
        //testing update progess for completing 1 out of 1 time
        habit.updateNumDone();
        assertEquals(1.0, habit.getProgress());
        //testing update progess for completing 1 out of 2 time
        habit.updateNumNotDone();
        assertEquals(0.5, habit.getProgress());
        //testing update progess for completing 3 out of 4 time
        habit.updateNumDone();
        habit.updateNumDone();
        assertEquals(0.75, habit.getProgress());
    }
}
