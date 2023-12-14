package com.example.project;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class AddPersonTest {
    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void add_person() {

        String name = "test";
        String birthday = "2000-01-01";
        String phone = "1234567890";

        onView(withId(R.id.fab)).perform(click());

        //put info into fields
        onView(withId(R.id.editTextName)).perform(click()).perform(typeText(name),closeSoftKeyboard());
        onView(withId(R.id.editBirthdate)).perform(click()).perform(typeText(birthday),closeSoftKeyboard());
        onView(withId(R.id.editPhoneNumber)).perform(click()).perform(typeText(phone),closeSoftKeyboard());
        onView(withId(R.id.buttonAddPerson)).perform(click());

        //check that the person was added
        onView(withId(R.id.home)).perform(click());

        onView(withId(R.id.showAll2)).perform(click());

        //scroll to bottom of list
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.scrollToLastPosition());


        //check that the person with the name was added
        onView(withId(R.id.recyclerView)).check(matches(hasDescendant(withText(name))));

    }

}
