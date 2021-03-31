package com.example.vehicle_policy.ui.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.VehiclePolicyViewModel
import com.example.vehicle_policy.util.ImageViewHasDrawableMatcher.hasDrawable
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
import org.mockito.Mockito
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class VehiclePolicyFragmentTest {

    @get:Rule
    val hiltTest = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var testFragmentFactory: TestVehiclePolicyFactory
    var testViewModel: VehiclePolicyViewModel? = null
    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltTest.inject()

        navController = Mockito.mock(NavController::class.java)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

/*    @Test
    fun pressBackButton_popBackStack() {
        launchFragmentInHiltContainer<VehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //pressBack()
        onView(withId(R.id.iv_back_vp)).perform(click())
        verify(navController).popBackStack()
    }*/

    @Test
    fun clickSearchCompany_navigateToSearchVehiclePolicyCompanyFragment() {
        launchFragmentInHiltContainer<VehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.iv_searchCompany_vp)).perform(click())
        verify(navController).navigate(VehiclePolicyFragmentDirections.actionVehiclePolicyFragmentToSearchVehiclePolicyCompanyFragment())
    }

    @Test
    fun test_OnItemClick_navigateToListVehiclePolicyFragment() {

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC", 1, id = 1)
        launchFragmentInHiltContainer<VehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)

            onItemClick(vehiclePolicyCompany)
        }

        verify(navController).navigate(VehiclePolicyFragmentDirections.actionVehiclePolicyFragmentToListVehiclePolicyFragment(vehiclePolicyCompany))
    }

    @Test
    fun test_Recyclerview() {

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC", R.drawable.hcl1, id = 1)
        runBlockingTest {
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicyCompanys(vehiclePolicyCompany)
        }

        launchFragmentInHiltContainer<VehiclePolicyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //Check Recyclerview isDisplay or not
        onView(withId(R.id.recyclerView_vp)).check(matches(isDisplayed()))

        //click Recyclerview position 1 item
        onView(withId(R.id.recyclerView_vp)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // then check correct navigate
        verify(navController).navigate(VehiclePolicyFragmentDirections.actionVehiclePolicyFragmentToListVehiclePolicyFragment(             vehiclePolicyCompany))

        // Check item at position 1 has "Some content"
        onView(withId(R.id.tv_companyName_vp_item)).check(matches(withText("LIC")))
        onView(withId(R.id.iv_companyIcon_vp_item)).check(matches(hasDrawable()))
    }
}