package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.StringBuilder

@ExperimentalCoroutinesApi
class ShowVehiclePolicyViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShowVehiclePolicyViewModel
    private lateinit var state: SavedStateHandle

    @Before
    fun setup() {
        state = SavedStateHandle()
        viewModel = ShowVehiclePolicyViewModel(state)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check SaveStateHandle`(){

        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456","Full",id = 1)
        state.set("showVehiclePolicy",vehiclePolicy)
        viewModel = ShowVehiclePolicyViewModel(state)

        viewModel.apply {
            assertThat(companyName).contains("LIC")
            assertThat(policyNumber).contains("123")
            assertThat(vehicleNumber).contains("GJ")
            assertThat(vehicleName).contains("Maruti")
            assertThat(purchaseDate).isEqualTo(12345L)
            assertThat(expiryDate).isEqualTo(67890L)
            assertThat(policyAmount).contains("456")
            assertThat(policyType).contains("Full")
        }
    }

    @Test
    fun `check copyClipBord StringBuilder`(){
        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456","Full",id = 1)
        state.set("showVehiclePolicy",vehiclePolicy)
        viewModel = ShowVehiclePolicyViewModel(state)

        val getCopyClipBord = viewModel.copyClipBoard()

        val copyText = StringBuilder()
            copyText.append("Company Name : LIC \n" +
                        "Policy Number : 123 \n" +
                        "Vehicle Number : GJ \n" +
                        "Vehicle Name : Maruti \n" +
                        "Purchase Date : 12345 \n" +
                        "Expiry Date : 67890 \n" +
                        "Policy Amount : 456 \n" +
                        "Policy Type : Full \n")

        assertThat(copyText.toString()).isEqualTo(getCopyClipBord.toString())
    }
}