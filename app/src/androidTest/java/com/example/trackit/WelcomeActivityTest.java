package com.example.trackit;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.trackit.LoginAndRegister.LoginActivity;
import com.example.trackit.LoginAndRegister.RegisterActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;

/**
 * Test class for WelcomeActivity. All the UI tests are written here. Robotium test framework is used
 */

public class WelcomeActivityTest {
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
     * Checks if clicking LOGIN button starts login activity
     */
    @Test
    public void checkLoginButton()
    {
        solo.assertCurrentActivity("Wrong Activity", WelcomeActivity.class);
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Checks if clicking LOGIN button starts login activity
     */
    @Test
    public void checkRegisterButton()
    {
        solo.assertCurrentActivity("Wrong Activity", WelcomeActivity.class);
        solo.clickOnButton("Register");
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


