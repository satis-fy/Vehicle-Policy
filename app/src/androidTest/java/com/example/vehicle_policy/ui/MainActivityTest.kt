package com.example.vehicle_policy.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.SmallTest
import com.example.vehicle_policy.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@SmallTest
@HiltAndroidTest
class MainActivityTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun test_isMainActivityInView(){
        var activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.MainActivity)).check(matches(isDisplayed()))
    }
}