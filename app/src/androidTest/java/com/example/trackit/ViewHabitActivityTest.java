package com.example.trackit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.trackit.Events.ViewEventsForHabitActivity;
import com.example.trackit.Habits.EditHabitActivity;
import com.example.trackit.Habits.ViewHabitActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;

/**
 * Test class for ViewHabitActivity. All the UI tests are written here. Robotium test framework is used
 */

public class ViewHabitActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class,true,true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Login");
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser");
        solo.waitForText("testUser", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnText("Existing habit 3");
        solo.clickOnView((TextView) solo.getView(R.id.view_details));
    }

    /**
     * Gets the activity
     * @throw Exception
     */
    @Test
    public void start() throws Exception
    {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if a habit is successfully displayed
     */
    @Test
    public void viewHabit()
    {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        assertTrue(solo.searchText("Existing habit 3"));
        assertTrue(solo.searchText("Test 3 for view and delete"));
        assertTrue(solo.searchText("10/30/2021"));
        assertTrue(solo.searchText("M"));
        assertTrue(solo.searchText("T"));
        assertTrue(solo.searchText("W"));
        assertTrue(solo.searchText("R"));
        assertTrue(solo.searchText("F"));
        assertTrue(solo.searchText("S"));
        assertTrue(solo.searchText("Su"));
    }

    /**
     * Checks if clicking opens edit habit button opens Edit habit activity
     */
    @Test
    public void clickEditHabit()
    {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnText("Edit Habit");
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
    }

    /**
     * Checks if clisking opens View events button shows coming soon snack bar
     */
    @Test
    public void clickViewEvent()
    {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnText("View Event");
        solo.assertCurrentActivity("Wrong Activity", ViewEventsForHabitActivity.class);
    }

    /**
     * Checks if clicking delete habit deletes the habit from today's habit list
     */
    @Test
    public void clickDeleteHabit()
    {
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnText("Delete Habit");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        assertFalse(solo.searchText("Existing habit 3"));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}



