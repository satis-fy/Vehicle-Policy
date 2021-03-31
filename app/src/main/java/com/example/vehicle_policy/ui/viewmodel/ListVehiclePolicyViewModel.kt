package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.repository.VehiclePolicyRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ListVehiclePolicyViewModel @ViewModelInject constructor(
    private val vehiclePolicyRepository: VehiclePolicyRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<Event>()
    val event = eventChannel.receiveAsFlow()

    private val vehiclePolicyArgs = state.get<VehiclePolicyCompanys>("singleCompanyNameLogo")
    val companyName = state.get<String>("companyName") ?: vehiclePolicyArgs?.companyName ?: "Lic Insurance"
    val companyLogo = state.get<Int>("companyLogo") ?: vehiclePolicyArgs?.companyLogo ?: 0

    val vehiclePolicyByCompanyName = vehiclePolicyRepository.getPolicyByCompany(companyName).asLiveData()

    fun onPolicyDelete(vehiclePolicy: VehiclePolicy) = viewModelScope.launch {
        vehiclePolicyRepository.deleteVehiclePolicy(vehiclePolicy)
        eventChannel.send(Event.ShowDeletePolicyDialog(vehiclePolicy))
    }

    sealed class Event{
        data class ShowDeletePolicyDialog(val vehiclePolicy: VehiclePolicy): Event()
    }
}