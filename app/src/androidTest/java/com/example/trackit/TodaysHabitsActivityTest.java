package com.example.trackit;

import android.app.Activity;
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
        //testing logging in
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
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_search));
        solo.assertCurrentActivity("Wrong Activity", UserSearchActivity.class);
    }

    /**
     * Checks if clicking notification navigation bar icon starts search activity
     */
    @Test
    public void clickMenuItemNotification()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_notification));
        solo.waitForText("Coming soon", 1, 2000);
    }

    /**
     * Checks if clicking user profile navigation bar icon starts search activity
     */
    @Test
    public void clickMenuItemProfile()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_bar_profile));
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
    }

    @Test
    public void clickAddButton()
    {
        //testing logging in
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.add_button));
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


