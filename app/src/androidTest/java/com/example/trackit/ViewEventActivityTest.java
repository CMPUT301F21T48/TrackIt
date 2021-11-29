package com.example.trackit;

import static org.junit.Assert.assertTrue;

import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewEventActivityTest {
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
        solo.enterText((EditText) solo.getView(R.id.login_username), "Gerrie");
        solo.waitForText("Gerrie", 1, 2000);
        solo.enterText((EditText) solo.getView(R.id.login_password), "Gerrie");
        solo.waitForText("Gerrie", 1, 2000);
        solo.clickOnText("Login");
        solo.clickOnText("asdfghjk");
        solo.clickOnText("View habit details");
    }




    /**
     * Checks if an event is successfully displayed
     */
    @Test
    public void viewEvent()
    {
        solo.clickOnText("View Events");
        solo.clickOnText("11/29/2021");
        assertTrue(solo.searchText("Hi guys"));
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
