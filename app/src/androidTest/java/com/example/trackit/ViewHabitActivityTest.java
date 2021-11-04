package com.example.trackit;

import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Test class for WelcomeActivity. All the UI tests are written here. Robotium test framework is used
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
        solo.clickOnText("Existing Habit 3");
        solo.clickOnText("View habit details");
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
        solo.waitForText("Existing habit 3", 1, 2000);
        solo.waitForText("Test 3 for view and delete", 1, 2000);
        solo.waitForText("10/30/2021", 1, 2000);
        solo.waitForText("Monday", 1, 2000);
        solo.waitForText("Tuesday", 1, 2000);
        solo.waitForText("Wednesday", 1, 2000);
        solo.waitForText("Thursday", 1, 2000);
        solo.waitForText("Friday", 1, 2000);
        solo.waitForText("Saturday", 1, 2000);
        solo.waitForText("Sunday", 1, 2000);
    }

    /**
     * Checks if clisking opens edit habit button opens Edit habit activity
     */
    @Test
    public void clickEditHabit()
    {
        //testing logging in
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
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
        solo.clickOnText("View Event");
        solo.waitForText("Coming soon");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
    }

    /**
     * Checks if clisking delete habit deletes the habit from today's habit list
     */
    @Test
    public void clickDeleteHabit()
    {
        //testing logging in
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



