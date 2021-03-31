package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.getOrAwaitValueTest
import com.example.vehicle_policy.repository.FakeVehiclePolicyRepository
import com.example.vehicle_policy.util.MainCoroutineRule
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
class VehiclePolicyViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: VehiclePolicyViewModel
    private val fakeVehiclePolicyRepository = FakeVehiclePolicyRepository()

    @Before
    fun setup() {
        viewModel = VehiclePolicyViewModel(fakeVehiclePolicyRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get all vehicle policy company list`(){
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",1,id = 1)

        mainCoroutineRule.launch {
            fakeVehiclePolicyRepository.addVehiclePolicyCompanys(vehiclePolicyCompany)
        }

        val vehicleCompany = viewModel.getAllVehiclePolicyCompany.getOrAwaitValueTest()
        assertThat(vehicleCompany).contains(vehiclePolicyCompany)
    }
}