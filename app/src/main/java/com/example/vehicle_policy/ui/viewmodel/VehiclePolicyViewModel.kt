package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.vehicle_policy.repository.VehiclePolicyRepository

class VehiclePolicyViewModel @ViewModelInject constructor(
    vehiclePolicyRepository: VehiclePolicyRepository
) : ViewModel() {

    val getAllVehiclePolicyCompany = vehiclePolicyRepository.getAllVehiclePolicyCompany().asLiveData()
}