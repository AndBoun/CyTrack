package com.example.CyTrack

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.CyTrack.Startup.LoginActivity
import org.hamcrest.CoreMatchers.anything
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AndrewInstrumentedTest {

    val SIMULATED_DELAY_MS: Long = 1000

    private lateinit var idlingResource: SimpleIdlingResource

    @get:Rule
//    var activityScenarioRule = ActivityScenarioRule(LoginActivity::class.java)
    var activityTestRule = ActivityTestRule(LoginActivity::class.java)


    @Test
    fun loginSuccess() {

        onView(withId(R.id.Username_field))
            .perform(typeText("rat"))

        onView(withId(R.id.Password_field))
            .perform(typeText("rata"), closeSoftKeyboard())

        onView(withId(R.id.login_button)).perform(click())

        try {
            Thread.sleep(SIMULATED_DELAY_MS)
        } catch (e: InterruptedException) {
        }


        onView(withId(R.id.helloTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun loginFailure() {

        onView(withId(R.id.Username_field))
            .perform(typeText("cata"))

        onView(withId(R.id.Password_field))
            .perform(typeText("cat"), closeSoftKeyboard())

        onView(withId(R.id.login_button)).perform(click())

        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.signUp_button)).check(matches(isDisplayed()))
    }

    @Test
    fun createAccountSuccess() {

        onView(withId(R.id.signUp_button)).perform(click())

        onView(withId(R.id.InputUsername))
            .perform(typeText("AtesterUnit"))

        onView(withId(R.id.InputPassword))
            .perform(typeText("1234"))

        onView(withId(R.id.InputPasswordAgain))
            .perform(typeText("1234"))

        onView(withId(R.id.inputFirstName))
            .perform(typeText("Carl"))

        onView(withId(R.id.inputLastName))
            .perform(typeText("Junior"))

        onView(withId(R.id.inputAge))
            .perform(typeText("22"))

        onView(withId(R.id.genderSpinner)).perform(click())
        onData(anything()).atPosition(0).perform(click())

        onView(withId(R.id.resetButton)).perform(click())

        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.signUp_button)).check(matches(isDisplayed()))
    }

    @Test
    fun resetPasswordSuccess(){

        onView(withId(R.id.forgotPassword_button)).perform(click())

        onView(withId(R.id.InputUsername))
            .perform(typeText("rat"))

        onView(withId(R.id.InputPassword))
            .perform(typeText("newpassword"))

        onView(withId(R.id.InputPasswordAgain))
            .perform(typeText("newpassword"))

        onView(withId(R.id.resetButton)).perform(click())

        try {
            Thread.sleep(SIMULATED_DELAY_MS)
        } catch (e: InterruptedException) {
        }

        onView(withId(R.id.login_button)).check(matches(isDisplayed()))
        onView(withId(R.id.signUp_button)).check(matches(isDisplayed()))
    }

}