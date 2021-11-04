package com.example.trackit;

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

public class AddHabitActivityTest {
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
        solo.clickOnView(solo.getView(R.id.add_button));
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
     * Checks if a new habit is added successfully
     */
    @Test
    public void addHabitForToday()
    {
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_title), "Test habit title");
        solo.waitForText("Test habit title", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.add_reason), "Test habit reason");
        solo.waitForText("Test habit reason", 1, 2000);
        solo.setDatePicker((DatePicker) solo.getView(R.id.select_start_date), 2021, 11-1, 04);
        solo.clickOnView(solo.getView(R.id.button_select_date));
        solo.waitForText("11/04/2021", 1, 2000);
        solo.clickOnView(solo.getView(R.id.checkbox_monday));
        solo.clickOnView(solo.getView(R.id.checkbox_tuesday));
        solo.clickOnView(solo.getView(R.id.checkbox_wednesday));
        solo.clickOnView(solo.getView(R.id.checkbox_thursday));
        solo.clickOnView(solo.getView(R.id.checkbox_friday));
        solo.clickOnView(solo.getView(R.id.checkbox_saturday));
        solo.clickOnView(solo.getView(R.id.checkbox_sunday));
        solo.clickOnButton("Add Habit");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.waitForText("Test habit reason", 1, 2000);
        solo.waitForText("Repeat: M T W R F S Su", 1, 2000);
        solo.waitForText("Reason: Test habit reason", 1, 2000);
    }

    /**
     * Checks if empty title field shows error
     */
    @Test
    public void emptyTitleField()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_reason), "Test habit reason");
        solo.waitForText("Test habit reason", 1, 2000);
        solo.setDatePicker((DatePicker) solo.getView(R.id.select_start_date), 2021, 11-1, 04);
        solo.clickOnView(solo.getView(R.id.button_select_date));
        solo.waitForText("11/04/2021", 1, 2000);
        solo.clickOnView(solo.getView(R.id.checkbox_monday));
        solo.clickOnView(solo.getView(R.id.checkbox_tuesday));
        solo.clickOnView(solo.getView(R.id.checkbox_wednesday));
        solo.clickOnView(solo.getView(R.id.checkbox_thursday));
        solo.clickOnView(solo.getView(R.id.checkbox_friday));
        solo.clickOnView(solo.getView(R.id.checkbox_saturday));
        solo.clickOnView(solo.getView(R.id.checkbox_sunday));
        solo.clickOnButton("Add Habit");
        solo.waitForText("Do not leave any field(s) empty", 1, 2000);
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
    }

    /**
     * Checks if empty reason field shows error
     */
    @Test
    public void emptyReasonField()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_title), "Test habit title");
        solo.waitForText("Test habit title", 1, 2000);
        solo.setDatePicker((DatePicker) solo.getView(R.id.select_start_date), 2021, 11-1, 04);
        solo.clickOnView(solo.getView(R.id.button_select_date));
        solo.waitForText("11/04/2021", 1, 2000);
        solo.clickOnView(solo.getView(R.id.checkbox_monday));
        solo.clickOnView(solo.getView(R.id.checkbox_tuesday));
        solo.clickOnView(solo.getView(R.id.checkbox_wednesday));
        solo.clickOnView(solo.getView(R.id.checkbox_thursday));
        solo.clickOnView(solo.getView(R.id.checkbox_friday));
        solo.clickOnView(solo.getView(R.id.checkbox_saturday));
        solo.clickOnView(solo.getView(R.id.checkbox_sunday));
        solo.clickOnButton("Add Habit");
        solo.waitForText("Do not leave any field(s) empty", 1, 2000);
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
    }

    /**
     * Checks if error is showed if date is not entered
     */
    @Test
    public void emptyDateField()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_title), "Test habit title");
        solo.waitForText("Test habit title", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.add_reason), "Test habit reason");
        solo.waitForText("Test habit reason", 1, 2000);
        solo.setDatePicker((DatePicker) solo.getView(R.id.select_start_date), 2021, 11-1, 04);
        solo.clickOnView(solo.getView(R.id.button_select_date));
        solo.waitForText("11/04/2021", 1, 2000);
        solo.clickOnButton("Add Habit");
        solo.waitForText("Do not leave any field(s) empty", 1, 2000);
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
    }

    /**
     * Checks if error is showed if date is not entered
     */
    @Test
    public void emptyRepeatField()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.add_title), "Test habit title");
        solo.waitForText("Test habit title", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.add_reason), "Test habit reason");
        solo.waitForText("Test habit reason", 1, 2000);
        solo.clickOnView(solo.getView(R.id.checkbox_monday));
        solo.clickOnView(solo.getView(R.id.checkbox_tuesday));
        solo.clickOnView(solo.getView(R.id.checkbox_wednesday));
        solo.clickOnView(solo.getView(R.id.checkbox_thursday));
        solo.clickOnView(solo.getView(R.id.checkbox_friday));
        solo.clickOnView(solo.getView(R.id.checkbox_saturday));
        solo.clickOnView(solo.getView(R.id.checkbox_sunday));
        solo.clickOnButton("Add Habit");
        solo.waitForText("Do not leave any field(s) empty", 1, 2000);
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
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



