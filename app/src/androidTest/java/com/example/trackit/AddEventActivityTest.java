package com.example.trackit;

import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.example.trackit.Habits.AddHabitActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;

/**
 * Test class for AddEventActivity. All the UI tests are written here. Robotium test framework is used
 * As such tests which require use of two application such as the photograph taking can't be performed
 * (https://github.com/RobotiumTech/robotium/wiki/Questions-&-Answers)
 */

public class AddEventActivityTest {
    private Solo solo;

    protected void makeHabitForToday()
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
        solo.clickOnView(solo.getView(R.id.privacy_button_private));
        solo.clickOnButton("Add Habit");
    }

    @Rule
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class,true,true);

    @Rule
    public GrantPermissionRule automationPermissionRule
            = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception
    {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Login");
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser3");
        solo.waitForText("testUser3", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.clickOnView(solo.getView(R.id.login));
        solo.clickOnView(solo.getView(R.id.add_button));
        // Can't use it right now cause of the layout
        makeHabitForToday();
        solo.clickOnText("Test habit title");
        solo.sleep(1500);

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
     * Checks if event added with no fields
     */
    @Test
    public void emptyAllFields()
    {
        solo.clickOnView(solo.getView(R.id.button_done));
        solo.clickOnView(solo.getView(R.id.button_skip));
    }

    /**
     * Checks if a new event is added successfully
     */
    @Test
    public void addEventWithCommentOnly()
    {
        solo.clickOnView(solo.getView(R.id.button_done));

        solo.enterText((EditText) solo.getView(R.id.add_comment), "That was great!");
        solo.waitForText("That was great!", 1, 2000);
        solo.clickOnView(solo.getView(R.id.button_add_habit));

    }

    /**
     * Checks if empty reason field shows error
     */
    @Test
    public void addEventWithLocationOnly()
    {
        solo.clickOnView(solo.getView(R.id.button_done));

        solo.clickOnView(solo.getView(R.id.button_location));
        solo.clickOnView(solo.getView(R.id.button_add_habit));
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



