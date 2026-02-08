package com.example.mihailoprojekat

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.mihailoprojekat", appContext.packageName)
    }

    @Test
    fun register_with_invalid_email() {
        onView(withId(R.id.emailText)).perform(typeText("invalidemail"))
        onView(withId(R.id.passwordText)).perform(typeText("123456"))
        onView(withId(R.id.emailText)).perform(closeSoftKeyboard())
        onView(withId(R.id.registerBtn)).perform(click())
        Thread.sleep(1000)
    }

    @Test
    fun register_with_invalid_password() {
        onView(withId(R.id.emailText)).perform(typeText("test123@gmail.com"))
        onView(withId(R.id.passwordText)).perform(typeText("123"))
        onView(withId(R.id.emailText)).perform(closeSoftKeyboard())
        onView(withId(R.id.registerBtn)).perform(click())
        Thread.sleep(1000)
    }

    @Test
    fun register_with_valid_credentials() {
        onView(withId(R.id.emailText)).perform(typeText("test123@gmail.com"))
        onView(withId(R.id.passwordText)).perform(typeText("123456"))
        onView(withId(R.id.emailText)).perform(closeSoftKeyboard())
        onView(withId(R.id.registerBtn)).perform(click())
        Thread.sleep(2000)
    }

    @Test
    fun can_clear_email_field() {
        onView(withId(R.id.emailText)).perform(typeText("test@test.com"))
        onView(withId(R.id.emailText)).perform(clearText())
        onView(withId(R.id.emailText)).perform(closeSoftKeyboard())
    }
}