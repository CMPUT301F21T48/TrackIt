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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Test class for WelcomeActivity. All the UI tests are written here. Robotium test framework is used
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EditHabitActivityTest {
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
        solo.clickOnText("Existing habit 4");
        solo.clickOnText("View habit details");
        solo.clickOnText("Edit Habit");
    }

    /**
     * Gets the activity
     * @throw Exception
     */
    @Test
    public void aStart() throws Exception
    {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if the existing details are successfully displayed
     */
    @Test
    public void bEditHabitForToday()
    {
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.waitForText("Existing habit 4", 1, 2000);
        solo.waitForText("Test 4 for edit", 1, 2000);
        solo.waitForText("10/30/2021", 1, 2000);
        solo.isCheckBoxChecked("M");
        solo.isCheckBoxChecked("T");
        solo.isCheckBoxChecked("W");
        solo.isCheckBoxChecked("R");
        solo.isCheckBoxChecked("S");
        solo.isCheckBoxChecked("Su");

    }

    /**
     * Checks if title is edited correctly
     */
    @Test
    public void gEditTitleTest()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.add_title));
        solo.enterText((EditText) solo.getView(R.id.add_title), "Existing habit 4-1");
        solo.clickOnButton("Save Changes");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.waitForText("Existing habit 4-1", 1, 2000);
    }

    /**
     * Checks if reason is edited correctly
     */
    @Test
    public void cEditReasonTest()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        solo.clearEditText((EditText) solo.getView(R.id.add_reason));
        solo.enterText((EditText) solo.getView(R.id.add_reason), "Test 4-1 for edit");
        solo.clickOnButton("Save Changes");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.waitForText("Test 4-1 for edit", 1, 2000);
    }

    /**
     * Checks if date is edited correctly
     */
    @Test
    public void dEditDateTest()
    {
        //testing logging in
        solo.setDatePicker((DatePicker) solo.getView(R.id.select_start_date), 2021, 11-1, 10);
        solo.clickOnView(solo.getView(R.id.button_select_date));
        solo.waitForText("11/10/2021", 1, 2000);
        solo.clickOnButton("Save Changes");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.waitForText("11/10/2021",1 , 2000);
    }

    /**
     * Checks if repeated days are edited correctly
     */
    @Test
    public void fEditDaysTest()
    {
        //testing logging in
        solo.clickOnView(solo.getView(R.id.checkbox_monday));
        solo.clickOnButton("Save Changes");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.waitForText("Repeat: T W R F S SU", 1, 2000);
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



