package com.example.trackit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Test class for TodaysHabitActivity. All the UI tests are written here. Robotium test framework is used
 */

public class TodaysHabitsActivityTest {
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
     * Checks if clicking today navigation bar icon stay on the same activity
     */
    @Test
    public void clickMenuItemToday()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_today));
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
    }

    /**
     * Checks if clicking search navigation bar icon starts search activity
     */
    @Test
    public void clickMenuItemSearch()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_search));
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);
    }

    /**
     * Checks if clicking notification navigation bar icon starts notifications activity
     */
    @Test
    public void clickMenuItemNotification()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_notification));
        solo.waitForText("Coming soon", 1, 2000);
    }

    /**
     * Checks if clicking user profile navigation bar icon starts user profile activity
     */
    @Test
    public void clickMenuItemProfile()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_profile));
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
    }

    /**
     * Checks if clicking add button starts add habit activity
     */
    @Test
    public void clickAddButton()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);
    }

    /**
     * Checks if clicking a habit opens the meny to view habit and clicking 'View detials' starts view habit activity
     */
    @Test
    public void clickExistingHabit()
    {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnText("Existing habit 1");
        assertTrue(solo.searchText("View habit details"));
        solo.clickOnText("View habit details");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
    }

    /**
     * Checks if clicking the done button updates progress bar accordingly
     */
    @Test
    public void updateProgressForDone() {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        //testing for doing habit 5 out of 5 times
        solo.clickOnText("Existing habit 1");
        ProgressBar bar = (ProgressBar) solo.getView(R.id.habit_progress);
        int prevProgress = bar.getProgress();
        solo.clickOnView(solo.getView(R.id.button_done));
        int newProgress = bar.getProgress();
        try {
            assertTrue(prevProgress <= newProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(solo.searchText("Existing habit 1"));
    }

    /**
     * Checks if clicking the not done button updates progress bar accordingly
     */
    @Test
    public void updateProgressForNotDone() {
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        //testing for doing habit 4 out of 8 times
        solo.clickOnText("Existing habit 2");
        ProgressBar bar = (ProgressBar) solo.getView(R.id.habit_progress);
        int prevProgress = bar.getProgress();
        solo.clickOnView(solo.getView(R.id.button_not_done));
        int newProgress = bar.getProgress();
        try {
            assertTrue(prevProgress >= newProgress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(solo.searchText("Existing habit 2"));
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