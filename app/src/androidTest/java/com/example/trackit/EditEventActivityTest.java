package com.example.trackit;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.Rule;

/**
 * Test class for EditEventActivity. All the UI tests are written here. Robotium test framework is used
 * As such tests which require use of two application such as the photograph taking can't be performed
 * (https://github.com/RobotiumTech/robotium/wiki/Questions-&-Answers)
 */

public class EditEventActivityTest {
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
    public void editAndVerify()
    {
        solo.clickOnText("View Events");
        solo.clickOnText("11/29/2021");

        // get Text
        assertTrue(solo.searchText("Hi 301"));
        solo.clickOnText("Edit Event");


        // Edit it
        EditText editText;
        editText = (EditText) solo.getView(R.id.add_comment);
        editText.getText().clear();
        solo.enterText((EditText) solo.getView(R.id.add_comment), "Hi 302");
        solo.waitForText("Hi 302", 1, 2000);
        solo.clickOnView(solo.getView(R.id.button_edit_event));
        solo.clickOnText("11/29/2021");
        assertTrue(solo.searchText("Hi 302"));


        // reset it
        solo.clickOnText("Edit Event");
        editText = (EditText) solo.getView(R.id.add_comment);
        editText.getText().clear();
        solo.enterText((EditText) solo.getView(R.id.add_comment), "Hi 301");
        solo.waitForText("Hi 301", 1, 2000);

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



