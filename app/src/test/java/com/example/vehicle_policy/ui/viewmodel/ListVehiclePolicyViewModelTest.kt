package com.example.vehicle_policy.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.example.vehicle_policy.data.VehiclePolicy
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

class ListVehiclePolicyViewModelTest{

    @get:Rule
    val instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ListVehiclePolicyViewModel
    private lateinit var state: SavedStateHandle
    private val fakeRepository = FakeVehiclePolicyRepository()

    @Before
    fun setup() {
        state = SavedStateHandle()
        viewModel = ListVehiclePolicyViewModel(fakeRepository,state)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get Vehicle Policy Data By CompanyName`() {
        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456","Full",id = 1)
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",1,id = 1)

        runBlockingTest{
            fakeRepository.addVehiclePolicy(vehiclePolicy)
            fakeRepository.addVehiclePolicy(VehiclePolicy("MAX",1,"123","GJ","Maruti",12345L,67890L,"456","Full",id = 2))
        }
        state.set("singleCompanyNameLogo",vehiclePolicyCompany)
        val getVehiclePolicy = viewModel.vehiclePolicyByCompanyName.getOrAwaitValueTest()
        assertThat(getVehiclePolicy[0].companyName).contains(vehiclePolicy.companyName)
//        assertThat(getVehiclePolicy).contains(vehiclePolicy)
    }

    @Test
    fun `delete vehicle policy`(){

        val vehiclePolicy = VehiclePolicy("LIC",1,"123","GJ","Maruti",12345L,67890L,"456","Full",id = 1)
        runBlockingTest {
            fakeRepository.addVehiclePolicy(vehiclePolicy)
        }

        viewModel.onPolicyDelete(vehiclePolicy)
        val getVehiclePolicy = viewModel.vehiclePolicyByCompanyName.getOrAwaitValueTest()
        assertThat(getVehiclePolicy).isEmpty()

        //check event of ShowDeletePolicyDialog
        assertThat(viewModel.event.asLiveData().getOrAwaitValueTest()).isEqualTo(ListVehiclePolicyViewModel.Event.ShowDeletePolicyDialog(vehiclePolicy))
    }

    @Test
    fun `check SaveStateHandle`(){
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",1,id = 1)
        state.set("singleCompanyNameLogo",vehiclePolicyCompany)
        viewModel = ListVehiclePolicyViewModel(fakeRepository,state)

        viewModel.apply {
            assertThat(companyName).isEqualTo("LIC")
            assertThat(companyLogo).isEqualTo(1)
        }
    }
}