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
 * Test class for SearchUserActivity. All the UI tests are written here. Robotium test framework is used
 */

public class SearchUserActivityTest {
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
        solo.enterText((EditText) solo.getView(R.id.login_username), "testUser2");
        solo.waitForText("testUser2", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnView(solo.getView(R.id.nav_bar_search));
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
     * Checks if search for another user if successful
     */
    @Test
    public void checkProfileItems() throws Exception{
        solo.assertCurrentActivity("Wrong Activity", SearchUserActivityTest.class);
        solo.enterText((EditText) solo.getView(R.id.squery), "testUser");
        solo.clickOnView(solo.getView(R.id.squerybutton));
        solo.clickOnText("testUser");
        solo.assertCurrentActivity("Wrong Activity", UserProfileActivity.class);
    }


}
