package com.example.vehicle_policy.ui.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import com.google.common.truth.Truth.assertThat
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.getOrAwaitValueAndroidTest
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.ListVehiclePolicyViewModel
import com.example.vehicle_policy.util.MyViewAction
import com.example.vehicle_policy.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ListVehiclePolicyFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var testFragmentFactory: TestVehiclePolicyFactory
    var testViewModel: ListVehiclePolicyViewModel? = null
    lateinit var navController: NavController

    val vehiclePolicy = VehiclePolicy("LIC", 1, "123", "GJ", "Maruti", 12345L, 67890L, "456", "Full", id = 1)

    @Before
    fun setup() {
        hiltRule.inject()

        navController = mock(NavController::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun clickAddVehiclePolicyButton_navigateToAddVehiclePolicyFragment() {

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC", 1, id = 0)
        testFragmentFactory.state.set("singleCompanyNameLogo", vehiclePolicyCompany)

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.iv_addPolicyIcon_vpl)).perform(click())
        verify(navController).navigate(ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToAddVehiclePolicyFragment(vehiclePolicyCompany, false))
    }

    @Test
    fun pressBackButton_popBackStack() {

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //pressBack()
        onView(withId(R.id.iv_back_vpl)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun test_OnItemClick_navigateToShowVehiclePolicyFragment() {

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)

            onItemClick(vehiclePolicy)
        }

        verify(navController).navigate(ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToShowVehiclePolicyFragment(vehiclePolicy))
    }

    @Test
    fun test_Recyclerview() {

        runBlockingTest {
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicy(VehiclePolicy("MAX", 1, "123", "GJ", "Maruti", 12345L, 67890L, "456", "Full", id = 2))
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicy(vehiclePolicy)
        }

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //Check Recyclerview isDisplay or not
        onView(withId(R.id.recyclerView_vpl)).check(matches(isDisplayed()))

        //click Recyclerview position 1 item
        onView(withId(R.id.recyclerView_vpl)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // then check correct navigate
        verify(navController).navigate(ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToShowVehiclePolicyFragment(vehiclePolicy))

        // Check item at position 1 has "Some content"
        onView(withId(R.id.tv_policyNumber_vpl_item)).check(matches(withText("123")))
        onView(withId(R.id.tv_vehicleNumber_vpl_item)).check(matches(withText("GJ")))
    }

    @Test
    fun select_Delete_Option_of_RecyclerView() {

        runBlockingTest {
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicy(vehiclePolicy)
        }

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //click Recyclerview ChildView Option Button position 1 item
        onView(withId(R.id.recyclerView_vpl)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.iv_options_vpl_item)))

        onView(withText("Delete")).perform(click())

        val getVehiclePolicy = testViewModel?.vehiclePolicyByCompanyName?.getOrAwaitValueAndroidTest()
        assertThat(getVehiclePolicy).isEmpty()

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(R.string.SnackBar_PolicyDelete)))
    }

    @Test
    fun select_Update_Option_of_RecyclerView() {

        runBlockingTest {
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicy(vehiclePolicy)
        }

        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //click Recyclerview ChildView Option Button position 1 item
        onView(withId(R.id.recyclerView_vpl)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.iv_options_vpl_item)))
        onView(withText("Edit")).perform(click())

        // then check correct navigate
        verify(navController).navigate(ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToUpdateVehiclePolicyFragment(vehiclePolicy))
    }

    @Test
    fun test_ListVehiclePolicy_title_CompanyName() {

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC", 1, id = 0)
        testFragmentFactory.state.set("singleCompanyNameLogo", vehiclePolicyCompany)
        launchFragmentInHiltContainer<ListVehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.tv_titleCompanyName_vpl)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_titleCompanyName_vpl)).check(matches(withText("LIC")))
    }
}