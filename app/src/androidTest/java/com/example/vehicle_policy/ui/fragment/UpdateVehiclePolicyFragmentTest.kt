package com.example.vehicle_policy.ui.fragment

import android.widget.DatePicker
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.repository.FakeAndroidTestVehiclePolicyRepository
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.UpdateVehiclePolicyViewModel
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
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class UpdateVehiclePolicyFragmentTest {

    @get:Rule
    val hiltTest = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRuleAndroidTest()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    lateinit var fakeRepository: FakeAndroidTestVehiclePolicyRepository
    lateinit var testViewModel: UpdateVehiclePolicyViewModel
    lateinit var state: SavedStateHandle
    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltTest.inject()

        state = SavedStateHandle()
        fakeRepository = FakeAndroidTestVehiclePolicyRepository()
        testViewModel = UpdateVehiclePolicyViewModel(fakeRepository, state)
        navController = Mockito.mock(NavController::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun pressBackButton_popBackStack() {

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        //pressBack()
        onView(withId(R.id.iv_back_vpu)).perform(click())
        Mockito.verify(navController).popBackStack()
    }

    @Test
    fun clickUpdatePolicyButton_checkUpdatePolicyButtonStatus_return_true() {

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.companyLogo = 1

        onView(withId(R.id.et_companyName_vpu)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpu)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpu)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpu)).perform(replaceText("Maruti"))

        onView(withId(R.id.et_purchaseDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_purchaseDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        onView(withId(R.id.et_expiryDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_expiryDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        onView(withId(R.id.et_policyAmount_vpu)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpu)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpu)).perform(replaceText("Full"))
        onView(withId(R.id.bt_updatePolicy_vpu)).perform(click())

        assertThat(testViewModel.updatePolicyButton).isTrue()
    }

    @Test
    fun checkAddPolicyButtonStatus_return_false_with_emptyField() {

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.companyLogo = 1

        onView(withId(R.id.et_companyName_vpu)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpu)).perform(replaceText(""))
        onView(withId(R.id.et_vehicleNumber_vpu)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpu)).perform(replaceText("Maruti"))

        onView(withId(R.id.et_purchaseDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_purchaseDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        onView(withId(R.id.et_expiryDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_expiryDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        onView(withId(R.id.et_policyAmount_vpu)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpu)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpu)).perform(replaceText("Full"))
        onView(withId(R.id.bt_updatePolicy_vpu)).perform(click())

        assertThat(testViewModel.updatePolicyButton).isFalse()
    }

    @Test
    fun checkUpdatePolicyButtonStatus_return_false_with_higherPurchaseDate() {

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.companyLogo = 1

        onView(withId(R.id.et_companyName_vpu)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpu)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpu)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpu)).perform(replaceText("Maruti"))

        onView(withId(R.id.et_purchaseDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 30)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_purchaseDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        onView(withId(R.id.et_expiryDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25)
        )
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_expiryDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        onView(withId(R.id.et_policyAmount_vpu)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpu)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpu)).perform(replaceText("Full"))
        onView(withId(R.id.bt_updatePolicy_vpu)).perform(click())

        assertThat(testViewModel.updatePolicyButton).isFalse()
    }

    @Test
    fun checkUpdatePolicyButtonStatus_return_false_with_companyLogoNotValid() {

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        testViewModel.companyLogo = 0

        onView(withId(R.id.et_companyName_vpu)).perform(replaceText("LIC"))
        onView(withId(R.id.et_policyNumber_vpu)).perform(replaceText("123"))
        onView(withId(R.id.et_vehicleNumber_vpu)).perform(replaceText("GJ"))
        onView(withId(R.id.et_vehicleName_vpu)).perform(replaceText("Maruti"))

        onView(withId(R.id.et_purchaseDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(1989, 8, 25))
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_purchaseDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        onView(withId(R.id.et_expiryDate_vpu)).perform(click())
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(1989, 8, 25))
        onView(ViewMatchers.withText("OK")).perform(click())
        onView(withId(R.id.et_expiryDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        onView(withId(R.id.et_policyAmount_vpu)).perform(replaceText("4560"))
        onView(withId(R.id.scroll_vpu)).perform(swipeUp())
        onView(withId(R.id.et_policyType_vpu)).perform(replaceText("Full"))
        onView(withId(R.id.bt_updatePolicy_vpu)).perform(click())

        assertThat(testViewModel.updatePolicyButton).isFalse()
    }

    @Test
    fun selectCheckDate_PurchaseDate(){
        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.et_purchaseDate_vpu)).perform(click())

        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25))

        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Click on the "OK" button to confirm and close the dialog
        onView(ViewMatchers.withText("OK")).perform(click())

        onView(withId(R.id.et_purchaseDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.purchaseDate)))

        assertThat(convertLongToTime(testViewModel.purchaseDate)).isEqualTo("25/08/1989")
    }

    @Test
    fun selectCheckDate_ExpiryDate(){

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.et_expiryDate_vpu)).perform(click())

        // Change the date of the DatePicker. Don't use "withId" as at runtime Android shares the DatePicker id between several sub-elements
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).perform(
            PickerActions.setDate(1989, 8, 25))

        //check DatePickerDialog is display or not (exist or not)
        onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Click on the "OK" button to confirm and close the dialog
        onView(ViewMatchers.withText("OK")).perform(click())

        onView(withId(R.id.et_expiryDate_vpu)).perform(replaceText(convertLongToTime(testViewModel.expiryDate)))

        assertThat(convertLongToTime(testViewModel.expiryDate)).isEqualTo("25/08/1989")
    }

    @Test
    fun test_Visibility(){

        launchFragmentInHiltContainer<UpdateVehiclePolicyFragment> {  }

        onView(withId(R.id.iv_back_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_addvehiclepolicy_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_compantname_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_companyName_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_policynumber_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_policyNumber_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_vehiclenumber_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_vehicleNumber_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_vehiclename_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_vehicleName_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_purchasedate_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_purchaseDate_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_expirydate_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_expiryDate_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.scroll_vpu)).perform(swipeUp())

        onView(withId(R.id.tv_policyamount_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_policyAmount_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.tv_policytype_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_policyType_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.bt_updatePolicy_vpu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}