package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.getOrAwaitValueTest
import com.example.vehicle_policy.repository.FakeVehiclePolicyRepository
import com.example.vehicle_policy.util.MainCoroutineRule
import com.example.vehicle_policy.util.Response
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddVehiclePolicyViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddVehiclePolicyViewModel
    private lateinit var state: SavedStateHandle
    private val fakeVehiclePolicyRepository = FakeVehiclePolicyRepository()

    @Before
    fun setup() {
        state = SavedStateHandle()
        viewModel = AddVehiclePolicyViewModel(fakeVehiclePolicyRepository,state)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert vehicle policy with valid input return Response Valid`() {
        viewModel.apply {
            companyName = "LIC"
            companyLogo = 1
            policyNumber = "123"
            vehicleNumber = "GJ"
            vehicleName = "Maruti"
            purchaseDate = 123L
            expiryDate = 234L
            policyAmount = "456"
            policyType = "Full"

            onSaveClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.Valid)
        }
    }

    @Test
    fun `insert vehicle policy with empty field return Response NotValid`() {
        viewModel.apply {
            companyName = "LIC"
            companyLogo = 1
            policyNumber = ""
            vehicleNumber = "GJ"
            vehicleName = "Maruti"
            purchaseDate = 123L
            expiryDate = 234L
            policyAmount = "456"
            policyType = "Full"

            onSaveClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.NotValid(isEmpty = true))
        }
    }

    @Test
    fun `insert vehicle policy with higher purchase date field return Response NotValid`() {
        viewModel.apply {
            companyName = "LIC"
            companyLogo = 1
            policyNumber = "123"
            vehicleNumber = "GJ"
            vehicleName = "Maruti"
            purchaseDate = 223L
            expiryDate = 134L
            policyAmount = "456"
            policyType = "Full"

            onSaveClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.NotValid(higherPurchaseDate = true))
        }
    }

    @Test
    fun `insert vehicle policy with addPolicy fun`() {
        viewModel.apply {
            val vehiclePolicy = VehiclePolicy(companyName, 1, policyNumber, vehicleNumber, vehicleName, purchaseDate, expiryDate, policyAmount)
            addPolicy(vehiclePolicy)

            val getVehiclePolicy = fakeVehiclePolicyRepository.getAllPolicy().asLiveData().getOrAwaitValueTest()
            assertThat(getVehiclePolicy).contains(vehiclePolicy)
        }
    }

    @Test
    fun `check SaveStateHandle`(){
        state.set("addVehiclePolicyCompanys", VehiclePolicyCompanys("LIC",1,id = 1))
        state.set("fromSearchVehiclePolicy",true)
        viewModel = AddVehiclePolicyViewModel(fakeVehiclePolicyRepository,state)

        viewModel.apply {
            assertThat(companyName).isEqualTo("LIC")
            assertThat(companyLogo).isEqualTo(1)
            assertThat(fromVehiclePolicyArgs).isTrue()
        }
    }
}