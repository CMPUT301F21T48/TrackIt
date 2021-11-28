package com.example.trackit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.trackit.R;
import com.example.trackit.WelcomeActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;

/**
 * Test class for UserProfileActivity. All the UI tests are written here. Robotium test framework is used
 */
public class UserProfileActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Login");
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser2");
        solo.waitForText("testUser2", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_profile));
    }

    /**
     * Gets the activity
     * @throw Exception
     */
    @Test
    public void Start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if the userName, followers and following is of the current logged in user is shown
     */
    @Test
    public void checkProfileItems() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        TextView name = (TextView) solo.getView(R.id.userNameView);
        assertEquals(name.getText().toString(), "testUser2");
        solo.waitForText("Habit", 1, 2000);
        TextView follower = (TextView) solo.getView(R.id.followerCount);
        assertEquals(follower.getText().toString(), "Followers: 0");
        TextView following = (TextView) solo.getView(R.id.followingCount);
        assertEquals(following.getText().toString(), "Following: 0");
    }

    /**
     * Checks if all the habits for the user are shown
     */
    @Test
    public void checkHabits() {
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        assertTrue(solo.searchText("Habit 1"));
        assertTrue(solo.searchText("Habit 2"));
        assertTrue(solo.searchText("Habit 3"));
        assertTrue(solo.searchText("Habit 4"));
    }

    /**
     * Checks if clicking on a habit take it to view habit activity
     */
    @Test
    public void viewHabit() {
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
        solo.clickOnText("Habit 1");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitActivity.class);
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


