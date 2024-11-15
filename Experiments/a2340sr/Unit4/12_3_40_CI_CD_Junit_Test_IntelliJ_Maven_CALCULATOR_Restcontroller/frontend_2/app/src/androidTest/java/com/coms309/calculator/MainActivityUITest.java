package com.coms309.calculator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testAdditionFunctionality() {
        onView(withId(R.id.number1)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.number2)).perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.result_text)).check(matches(withText("Result: 5")));
    }

    @Test
    public void testMultiplicationFunctionality() {
        onView(withId(R.id.number1)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.number2)).perform(typeText("3"), closeSoftKeyboard());
        onView(withId(R.id.multiply_button)).perform(click());
        onView(withId(R.id.result_text)).check(matches(withText("Result: 6")));
    }
}
