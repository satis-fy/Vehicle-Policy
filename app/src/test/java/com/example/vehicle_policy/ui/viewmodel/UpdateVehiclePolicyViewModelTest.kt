package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.getOrAwaitValueTest
import com.example.vehicle_policy.repository.FakeVehiclePolicyRepository
import com.example.vehicle_policy.util.MainCoroutineRule
import com.example.vehicle_policy.util.Response
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateVehiclePolicyViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: UpdateVehiclePolicyViewModel
    private lateinit var state: SavedStateHandle
    private lateinit var fakeRepository: FakeVehiclePolicyRepository

    @Before
    fun setup(){
        state = SavedStateHandle()
        fakeRepository = FakeVehiclePolicyRepository()
        viewModel = UpdateVehiclePolicyViewModel(fakeRepository,state)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown(){
        Dispatchers.resetMain()
    }

    @Test
    fun `update vehicle policy with valid input return Response Valid`() {
        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456",id = 1)
        state.set("UpdateVehiclePolicy",vehiclePolicy)
        viewModel = UpdateVehiclePolicyViewModel(fakeRepository,state)

        mainCoroutineRule.launch {
            fakeRepository.addVehiclePolicy(vehiclePolicy)
        }

        viewModel.apply {
            onUpdateClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.Valid)
        }
    }

    @Test
    fun `update vehicle policy with empty field return Response NotValid`() {
        val vehiclePolicy = VehiclePolicy("",1,"123","GJ","Maruti",12345L,67890L,"456",id = 1)
        state.set("UpdateVehiclePolicy",vehiclePolicy)
        viewModel = UpdateVehiclePolicyViewModel(fakeRepository,state)

        mainCoroutineRule.launch {
            fakeRepository.addVehiclePolicy(vehiclePolicy)
        }

        viewModel.apply {
            onUpdateClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.NotValid(isEmpty = true))
        }
    }

    @Test
    fun `update vehicle policy with higher purchase date field return Response NotValid`() {
        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",67890L,12345L,"456",id = 1)
        state.set("UpdateVehiclePolicy",vehiclePolicy)
        viewModel = UpdateVehiclePolicyViewModel(fakeRepository,state)

        mainCoroutineRule.launch {
            fakeRepository.addVehiclePolicy(vehiclePolicy)
        }

        viewModel.apply {
            onUpdateClick()
            assertThat(event.asLiveData().getOrAwaitValueTest()).isEqualTo(Response.NotValid(higherPurchaseDate = true))
        }
    }

    @Test
     fun `update vehicle policy with updatePolicy fun`() {
        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456",id = 1)

        mainCoroutineRule.launch {
            fakeRepository.addVehiclePolicy(vehiclePolicy)
        }

        val updateVehiclePolicy = VehiclePolicy("LIC", 1, "456", "GJ", "Maruti", 12345L, 67890L, "456", id = 1)
        viewModel.updatePolicy(updateVehiclePolicy)
        val getVehiclePolicy = fakeRepository.getAllPolicy().asLiveData().getOrAwaitValueTest()

        assertThat(getVehiclePolicy).contains(updateVehiclePolicy)
    }
}