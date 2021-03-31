package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.vehicle_policy.repository.VehiclePolicyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class SearchVehiclePolicyCompanysViewModel @ViewModelInject constructor(
    private val vehiclePolicyRepository: VehiclePolicyRepository
): ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val searchFlow = searchQuery.flatMapLatest {
        vehiclePolicyRepository.getVehiclePolicyCompanys(it)
    }

    val vehiclePolicyCompanys = searchFlow.asLiveData()
}