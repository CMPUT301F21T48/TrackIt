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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

/**
 * Test class for SearchUserActivity. All the UI tests are written here. Robotium test framework is used
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotificationActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<WelcomeActivity> rule =
            new ActivityTestRule<>(WelcomeActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnButton("Login");
    }

    /**
     * Gets the activity
     * @throw Exception
     */
    @Test
    public void aStart() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if requeting to follow a user changes the status to requested
     */
    @Test
    public void bCheckRequestToFollow() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser2");
        solo.waitForText("testUser2", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_search));
        solo.enterText((EditText) solo.getView(R.id.squery), "testUser");
        solo.clickOnView(solo.getView(R.id.squerybutton));
        solo.clickOnText("testUser");
        solo.clickOnView(solo.getView(R.id.followButton));
        assertTrue(solo.searchText("Requested"));
    }

    /**
     * Checks if the request to follows shows in notifications
     */
    @Test
    public void cCheckNotifications() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser");
        solo.waitForText("testUser", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_notification));
        assertTrue(solo.searchText("testUser2 has requested to follow you."));
        solo.clickOnText("testUser2 has requested to follow you.");
        solo.clickOnView(solo.getView(R.id.button_accept));
        assertFalse(solo.searchText("testUser2 has requested to follow you."));
    }

    /**
     * Checks if the followers count for the user being followed
     */
    @Test
    public void dCheckFollowerCount() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser");
        solo.waitForText("testUser", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_profile));
        solo.waitForText("Habit", 1, 2000);
        TextView follower = (TextView) solo.getView(R.id.followerCount);
        assertEquals(follower.getText().toString(), "Followers: 1");
        TextView following = (TextView) solo.getView(R.id.followingCount);
        assertEquals(following.getText().toString(), "Following: 0");
    }

    /**
     * Checks if the following count for the user following is updated correctly
     */
    @Test
    public void eCheckFollowingCount() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser2");
        solo.waitForText("testUser2", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_profile));
        solo.waitForText("Habit", 1, 2000);
        TextView follower = (TextView) solo.getView(R.id.followerCount);
        assertEquals(follower.getText().toString(), "Followers: 0");
        TextView following = (TextView) solo.getView(R.id.followingCount);
        assertEquals(following.getText().toString(), "Following: 1");
    }

    /**
     * Checks if the user is able to see the activities of user followed and if unfollowing them
     * makes the habits disappear again
     */
    @Test
    public void fCcheckHabitsUser() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser2");
        solo.waitForText("testUser2", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_search));
        solo.enterText((EditText) solo.getView(R.id.squery), "testUser");
        solo.clickOnView(solo.getView(R.id.squerybutton));
        solo.clickOnText("testUser");
        assertTrue(solo.searchText("Existing habit 1"));
        assertTrue(solo.searchText("Existing habit 2"));
        solo.clickOnView(solo.getView(R.id.followButton));
        solo.searchText("Habit");
        assertFalse(solo.searchText("Existing habit 1"));
        assertFalse(solo.searchText("Existing habit 2"));
    }

}
