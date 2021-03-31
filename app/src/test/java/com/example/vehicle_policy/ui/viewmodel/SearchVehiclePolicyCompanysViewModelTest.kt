package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.getOrAwaitValueTest
import com.example.vehicle_policy.repository.FakeVehiclePolicyRepository
import com.example.vehicle_policy.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchVehiclePolicyCompanysViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: SearchVehiclePolicyCompanysViewModel
    private val fakeRepository = FakeVehiclePolicyRepository()

    @Before
    fun setup() {
        viewModel = SearchVehiclePolicyCompanysViewModel(fakeRepository)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test search Query String`() = runBlockingTest{
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",1,id = 1)

        fakeRepository.addVehiclePolicyCompanys(vehiclePolicyCompany)

        val getVehiclePolicyCompany = viewModel.vehiclePolicyCompanys.getOrAwaitValueTest()
        assertThat(getVehiclePolicyCompany).contains(vehiclePolicyCompany)

    }
}