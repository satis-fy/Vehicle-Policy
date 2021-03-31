package com.example.vehicle_policy.ui.fragment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.ui.MainActivity
import com.example.vehicle_policy.ui.viewmodel.SearchVehiclePolicyCompanysViewModel
import com.example.vehicle_policy.util.ImageViewHasDrawableMatcher
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
class SearchVehiclePolicyCompanyFragmentTest{

    @get:Rule
    val hiltTest = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Inject
    lateinit var testFragmentFactory: TestVehiclePolicyFactory
    var testViewModel: SearchVehiclePolicyCompanysViewModel? = null
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

    @Test
    fun pressBackButton_popBackStack() {

        launchFragmentInHiltContainer<SearchVehiclePolicyCompanyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //pressBack()
        onView(ViewMatchers.withId(R.id.iv_back_vp_search)).perform(click())
        verify(navController).popBackStack()
    }

    @Test
    fun test_Recyclerview_click() {

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC", R.drawable.hcl1, id = 1)
        runBlockingTest {
            testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicyCompanys(vehiclePolicyCompany)
        }

        launchFragmentInHiltContainer<SearchVehiclePolicyCompanyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //Check Recyclerview isDisplay or not
        onView(ViewMatchers.withId(R.id.recyclerView_vp_search)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        //click Recyclerview position 1 item
        onView(ViewMatchers.withId(R.id.recyclerView_vp_search)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // then check correct navigate
        verify(navController).navigate(SearchVehiclePolicyCompanyFragmentDirections.actionSearchVehiclePolicyCompanyFragmentToAddVehiclePolicyFragment(vehiclePolicyCompany,true))

        // Check item at position 1 has "Some content"
        onView(withId(R.id.tv_companyName_vp_search)).check(matches(ViewMatchers.withText("LIC")))
        onView(withId(R.id.iv_companyLogo_vp_search)).check(matches(ImageViewHasDrawableMatcher.hasDrawable()))
    }

    @Test
    fun test_Search_query_filter_on_recyclerview_then_click_on_item() = runBlockingTest {

        val vehiclePolicyCompany1 = VehiclePolicyCompanys("Life Insurance Corporation – LIC", R.drawable.hcl1, id = 1)
        val vehiclePolicyCompany2 = VehiclePolicyCompanys("Aditya Birla Sun Life Insurance", R.drawable.hcl2, id = 2)
        val vehiclePolicyCompany3 = VehiclePolicyCompanys("HDFC Life Insurance Co. Ltd.", R.drawable.hcl13, id = 3)

        testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicyCompanys(vehiclePolicyCompany1)
        testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicyCompanys(vehiclePolicyCompany2)
        testFragmentFactory.fakeAndroidTestRepository.addVehiclePolicyCompanys(vehiclePolicyCompany3)

        launchFragmentInHiltContainer<SearchVehiclePolicyCompanyFragment>(
            fragmentFactory = testFragmentFactory
        ) {
            testViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        //Check Recyclerview isDisplay or not
        onView(withId(R.id.recyclerView_vp_search)).check(matches(ViewMatchers.isDisplayed()))

        //Apply Search Query On EditText
        onView(withId(R.id.et_searchCompany_vp_search)).perform(typeText("HDFC"))

        // Check item at position 1 has "Some content"
        onView(withId(R.id.tv_companyName_vp_search)).check(matches(ViewMatchers.withText("HDFC Life Insurance Co. Ltd.")))
        onView(withId(R.id.iv_companyLogo_vp_search)).check(matches(ImageViewHasDrawableMatcher.hasDrawable()))

        //click Recyclerview position 1 item
        onView(withId(R.id.recyclerView_vp_search)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // then check correct navigate
        verify(navController).navigate(SearchVehiclePolicyCompanyFragmentDirections.actionSearchVehiclePolicyCompanyFragmentToAddVehiclePolicyFragment(vehiclePolicyCompany3,true))
    }
}