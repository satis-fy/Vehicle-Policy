package com.example.vehicle_policy.ui.fragment

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.ShowVehiclePolicyViewModel
import com.example.vehicle_policy.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShowVehiclePolicyFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var testFragmentFactory: TestVehiclePolicyFactory
    var testViewModel: ShowVehiclePolicyViewModel? = null
    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()

        navController = Mockito.mock(NavController::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun pressBackButton_popBackStack() {

        launchFragmentInHiltContainer<ShowVehiclePolicyFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        //pressBack()
        onView(withId(R.id.iv_back_vps)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun test_data_of_showVehiclePolicy() {

        launchFragmentInHiltContainer<ShowVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.tv_companyName_vps)).check(matches(withText("LIC")))
        onView(withId(R.id.tv_policyNumber_vps)).check(matches(withText("123")))
        onView(withId(R.id.tv_vehicleNumber_vps)).check(matches(withText("GJ")))
        onView(withId(R.id.tv_vehicleName_vps)).check(matches(withText("Maruti")))
        onView(withId(R.id.tv_purchaseDate_vps)).check(matches(withText("28/03/2021")))
        onView(withId(R.id.tv_expiryDate_vps)).check(matches(withText("28/03/2021")))
        onView(withId(R.id.tv_policyAmount_vps)).check(matches(withText("456")))
        onView(withId(R.id.tv_policyType_vps)).check(matches(withText("Full")))
    }

    @Test
    fun test_click_copyIcon_copy_data_ClipBoard(){

        launchFragmentInHiltContainer<ShowVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.iv_copyClipBoardIcon_vps)).perform(click())

        // check Toast is Display with correct text
        // onView(withText(R.string.CopyTextToast)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
    }
}