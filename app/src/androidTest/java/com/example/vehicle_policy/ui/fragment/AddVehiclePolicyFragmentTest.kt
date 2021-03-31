package com.example.vehicle_policy.ui.fragment

import android.widget.DatePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.getOrAwaitValueAndroidTest
import com.example.vehicle_policy.repository.FakeAndroidTestVehiclePolicyRepository
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.AddVehiclePolicyViewModel
import com.example.vehicle_policy.util.MainCoroutineRuleAndroidTest
import com.example.vehicle_policy.util.convertLongToTime
import com.example.vehicle_policy.util.launchFragmentInHiltContainer
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class AddVehiclePolicyFragmentTest {

    @get:Rule
    val hiltTest = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRuleAndroidTest()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    lateinit var fakeRepository: FakeAndroidTestVehiclePolicyRepository
    lateinit var testViewModel: AddVehiclePolicyViewModel
    lateinit var state: SavedStateHandle
    lateinit var  navController:NavController

    @Before
    fun setup() {
        hiltTest.inject()

        state = SavedStateHandle()
        fakeRepository = FakeAndroidTestVehiclePolicyRepository()
        testViewModel = AddVehiclePolicyViewModel(fakeRepository, state)
        navController = mock(NavController::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        //pressBack()
        onView(withId(R.id.iv_back_vpi)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun clickSavePolicyButton_InsertDataIntoDatabase() {

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {}
        val vehiclePolicy = VehiclePolicy("LIC", 1, "123", "GJ", "Maruti", 12345, 67890, "4560", "Full", id = 1)
        testViewModel.addPolicy(vehiclePolicy)

        assertThat(fakeRepository.getAllPolicy().asLiveData().getOrAwaitValueAndroidTest())
            .contains(vehiclePolicy)
    }

    @Test
    fun clickAddPolicyButton_checkAddPolicyButtonStatus_return_true() {

            launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
                viewModel = testViewModel
            }

            testViewModel.apply {
                companyLogo = 1
                purchaseDate = 12345L
                expiryDate = 67890L
            }
            onView(withId(R.id.et_companyName_vpi)).perform(replaceText("LIC"))
            onView(withId(R.id.et_policyNumber_vpi)).perform(replaceText("123"))
            onView(withId(R.id.et_vehicleNumber_vpi)).perform(replaceText("GJ"))
            onView(withId(R.id.et_vehicleName_vpi)).perform(replaceText("Maruti"))
            onView(withId(R.id.et_purchaseDate_vpi)).perform(replaceText("01/02/2021"))
            onView(withId(R.id.et_expiryDate_vpi)).perform(replaceText("03/04/2021"))
            onView(withId(R.id.et_policyAmount_vpi)).perform(replaceText("4560"))
            onView(withId(R.id.scroll_vpi)).perform(swipeUp())
            onView(withId(R.id.et_policyType_vpi)).perform(replaceText("Full"))
            onView(withId(R.id.bt_addPolicy_vpi)).perform(click())

            assertThat(testViewModel.addPolicyButton).isTrue()
        }

    @Test
    fun checkAddPolicyButtonStatus_return_false_with_emptyField() {

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.apply {
            companyLogo = 1
            purchaseDate = 12345L
            expiryDate = 67890L
        }
        onView(withId(R.id.et_companyName_vpi)).perform(replaceText(""))
        onView(withId(R.id.et_policyNumber_vpi)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpi)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpi)).perform(replaceText("Maruti"))
        onView(withId(R.id.et_purchaseDate_vpi)).perform(replaceText("01/02/2021"))
        onView(withId(R.id.et_expiryDate_vpi)).perform(replaceText("03/04/2021"))
        onView(withId(R.id.et_policyAmount_vpi)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpi)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpi)).perform(replaceText("Full"))
        onView(withId(R.id.bt_addPolicy_vpi)).perform(click())

        assertThat(testViewModel.addPolicyButton).isFalse()
    }

    @Test
    fun checkAddPolicyButtonStatus_return_false_with_higherPurchaseDate() {

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.apply {
            companyLogo = 1
            purchaseDate = 67890L
            expiryDate = 12345L
        }
        onView(withId(R.id.et_companyName_vpi)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpi)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpi)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpi)).perform(replaceText("Maruti"))
        onView(withId(R.id.et_purchaseDate_vpi)).perform(replaceText("01/02/2021"))
        onView(withId(R.id.et_expiryDate_vpi)).perform(replaceText("03/04/2021"))
        onView(withId(R.id.et_policyAmount_vpi)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpi)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpi)).perform(replaceText("Full"))
        onView(withId(R.id.bt_addPolicy_vpi)).perform(click())

        assertThat(testViewModel.addPolicyButton).isFalse()
    }

    @Test
    fun checkAddPolicyButtonStatus_return_false_with_companyLogoNotValid() {

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.apply {
            companyLogo = 0
            purchaseDate = 12345L
            expiryDate = 67890L
        }
        onView(withId(R.id.et_companyName_vpi)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpi)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpi)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpi)).perform(replaceText("Maruti"))
        onView(withId(R.id.et_purchaseDate_vpi)).perform(replaceText("01/02/2021"))
        onView(withId(R.id.et_expiryDate_vpi)).perform(replaceText("03/04/2021"))
        onView(withId(R.id.et_policyAmount_vpi)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpi)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpi)).perform(replaceText("Full"))
        onView(withId(R.id.bt_addPolicy_vpi)).perform(click())

        assertThat(testViewModel.addPolicyButton).isFalse()
    }

    @Test
    fun selectCheckDate_PurchaseDate(){

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.et_purchaseDate_vpi)).perform(click())

        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25))

        //check DatePickerDialog is display or not (exist or not)
        // use .check(doesNotExist()) if view in GONE
        // use .check(matches(not(isDisplay()))) if view is INVISIBLE
        // use .check(matches(isDisplayed())) if view is VISIBLE
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).check(matches(isDisplayed()))

        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click())

        onView(withId(R.id.et_purchaseDate_vpi)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        assertThat(convertLongToTime(testViewModel.purchaseDate)).isEqualTo("25/08/1989")
    }

    @Test
    fun selectCheckDate_ExpiryDate(){

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.et_expiryDate_vpi)).perform(click())

        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25))

        //check DatePickerDialog is display or not (exist or not)
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name))).check(matches(isDisplayed()))

        // Click on the "OK" button to confirm and close the dialog
        onView(withText("OK")).perform(click())

        onView(withId(R.id.et_expiryDate_vpi)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        assertThat(convertLongToTime(testViewModel.expiryDate)).isEqualTo("25/08/1989")
    }

    @Test
    fun test_Visibility(){

        launchFragmentInHiltContainer<AddVehiclePolicyFragment> {  }

        onView(withId(R.id.iv_back_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_addvehiclepolicy_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_compantname_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_companyName_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_policynumber_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_policyNumber_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_vehiclenumber_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_vehicleNumber_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_vehiclename_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_vehicleName_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_purchasedate_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_purchaseDate_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_expirydate_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_expiryDate_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.scroll_vpi)).perform(swipeUp())

        onView(withId(R.id.tv_policyamount_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_policyAmount_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.tv_policytype_vpi)).check(matches(isDisplayed()))
        onView(withId(R.id.et_policyType_vpi)).check(matches(isDisplayed()))

        onView(withId(R.id.bt_addPolicy_vpi)).check(matches(isDisplayed()))
    }
}