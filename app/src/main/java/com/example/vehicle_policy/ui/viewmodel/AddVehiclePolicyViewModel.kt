package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.repository.VehiclePolicyRepository
import com.example.vehicle_policy.util.Response
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddVehiclePolicyViewModel @ViewModelInject constructor(
    private val vehiclePolicyRepository: VehiclePolicyRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<Response>()
    val event = eventChannel.receiveAsFlow()

    private val vehiclePolicyArgs = state.get<VehiclePolicyCompanys>("addVehiclePolicyCompanys")
    val fromVehiclePolicyArgs = state.get<Boolean>("fromSearchVehiclePolicy") ?: true

    var companyName = state.get<String>("companyNameVPI") ?: vehiclePolicyArgs?.companyName ?: ""
    var companyLogo = state.get<Int>("companyLogoVPI") ?: vehiclePolicyArgs?.companyLogo ?: 0

    var policyNumber = state.get<String>("policyNumberVPI") ?: ""
        set(value) {
            field = value
            state.set("policyNumberVPI", value)
        }
    var vehicleNumber = state.get<String>("vehiclePolicyVIP") ?: ""
        set(value) {
            field = value
            state.set("vehiclePolicyVIP", value)
        }
    var vehicleName = state.get<String>("vehicleNameVIP") ?: ""
        set(value) {
            field = value
            state.set("vehicleNameVIP", value)
        }
    var purchaseDate = state.get<Long>("purchaseDateVPI") ?: 1L
        set(value) {
            field = value
            state.set("purchaseDateVPI", value)
        }
    var expiryDate = state.get<Long>("expiryDateVPI") ?: 1L
        set(value) {
            field = value
            state.set("expiryDateVPI", value)
        }
    var policyAmount = state.get<String>("policyAmountVPI") ?: ""
        set(value) {
            field = value
            state.set("policyAmountVPI", value)
        }
    var policyType = state.get<String>("policyTypeVPI") ?: ""
        set(value) {
            field = value
            state.set("policyTypeVPI", value)
        }
    var addPolicyButton = state.get<Boolean>("addButtonVPI") ?: false
        set(value) {
            field = value
            state.set("addFileVPI", value)
        }

     fun addPolicy(vehiclePolicy: VehiclePolicy) = viewModelScope.launch {
        vehiclePolicyRepository.addVehiclePolicy(vehiclePolicy)
    }

    fun onSaveClick() = viewModelScope.launch {

        if (companyName.isEmpty() || companyLogo == 0 || policyNumber.isEmpty() || vehicleNumber.isEmpty() || vehicleName.isEmpty() || policyAmount.isEmpty()) {
            eventChannel.send(Response.NotValid(isEmpty = true))
        }else if(purchaseDate > expiryDate) {
            eventChannel.send(Response.NotValid(higherPurchaseDate = true))
        }

        val vehiclePolicy = VehiclePolicy(companyName, companyLogo, policyNumber, vehicleNumber, vehicleName, purchaseDate, expiryDate, policyAmount, policyType)
        addPolicy(vehiclePolicy)
        eventChannel.send(Response.Valid)
    }
}