package com.example.project;
import androidx.annotation.ContentView;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AddPerson {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void show_all_ui_check() {
        // Click on the add person button
        // Check that the add person page is displayed
        onView(withId(androidx.constraintlayout.widget.R.id.home)).perform(click());
        onView(withId(R.id.showAll)).perform(click());

        onView(withId(R.id.showAll2)).check(matches(isDisplayed()));


    }

    @Test
    public void add_person() {
        // Click on the add person button
        // Check that the add person page is displayed
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.addPerson2)).check(matches(isDisplayed()));
    }

}
