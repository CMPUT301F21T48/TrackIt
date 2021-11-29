package com.example.trackit;

import static org.junit.Assert.assertTrue;

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
 * Test class for RegisterActivity. All the UI tests are written here. Robotium test framework is used
 */

public class RegisterActivityTest {
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
        solo.clickOnButton("Register");
    }

    /**
     * Gets the activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception
    {
        Activity activity = rule.getActivity();
    }

    /**
     * Checks if register with unique username and password is successful and if user is able to logout
     */
    @Test
    public void registerTest()
    {
        //testing registering
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.register_username), "testNewUser");
        solo.waitForText("testNewUser", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.register_password), "testNewPassword");
        solo.waitForText("testNewPassword", 1, 2000);
        solo.clickOnText("Sign Up");
        solo.assertCurrentActivity("Wrong Activity", TodaysHabitsActivity.class);
        //testing logout
        solo.clickOnText("Logout");
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
    }

    /**
     * Checks if non unique username shows error
     */
    @Test
    public void checkNonUniqueUsername()
    {
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.register_username), "testUser");
        solo.waitForText("testUser", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.register_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Username already exists"));
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
    }

    /**
     * Checks if empty user name field raises error
     */
    @Test
    public void checkEmptyUserName()
    {
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.register_password), "testPassword");
        solo.waitForText("testPassword", 1, 2000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Username field cannot be empty"));
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
    }

    /**
     * Checks if empty password field raises error
     */
    @Test
    public void checkEmptyPassword()
    {
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
        solo.enterText((EditText) solo.getView(R.id.register_username), "testUser");
        solo.waitForText("testUser", 1, 2000);
        solo.clickOnText("Sign Up");
        assertTrue(solo.searchText("Password field cannot be empty"));
        solo.assertCurrentActivity("Wrong Activity", RegisterActivity.class);
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


