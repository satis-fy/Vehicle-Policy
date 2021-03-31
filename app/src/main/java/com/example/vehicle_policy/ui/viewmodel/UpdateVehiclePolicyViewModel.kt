package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.repository.VehiclePolicyRepository
import com.example.vehicle_policy.util.Response
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UpdateVehiclePolicyViewModel @ViewModelInject constructor(
    private val vehiclePolicyRepository: VehiclePolicyRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<Response>()
    val event = eventChannel.receiveAsFlow()

    private val vehiclePolicyArgs = state.get<VehiclePolicy>("UpdateVehiclePolicy")

    val id = vehiclePolicyArgs?.id ?: 0
    var companyLogo = vehiclePolicyArgs?.companyLogo ?: 0

    var companyName = state.get<String>("companyNameVPU") ?: vehiclePolicyArgs?.companyName ?: ""
        set(value) {
            field = value
            state.set("companyNameVPU", value)
        }
    var policyNumber = state.get<String>("policyNumberVPU") ?: vehiclePolicyArgs?.policyNumber ?: ""
        set(value) {
            field = value
            state.set("policyNumberVPU", value)
        }
    var vehicleNumber =
        state.get<String>("vehiclePolicyVPU") ?: vehiclePolicyArgs?.vehicleNumber ?: ""
        set(value) {
            field = value
            state.set("vehiclePolicyVPU", value)
        }
    var vehicleName = state.get<String>("vehicleNameVPU") ?: vehiclePolicyArgs?.vehicleName ?: ""
        set(value) {
            field = value
            state.set("vehicleNameVPU", value)
        }
    var purchaseDate = state.get<Long>("purchaseDateVPU") ?: vehiclePolicyArgs?.purchaseDate ?: 1L
        set(value) {
            field = value
            state.set("purchaseDateVPU", value)
        }
    var expiryDate = state.get<Long>("expiryDateVPU") ?: vehiclePolicyArgs?.expiryDate ?: 1L
        set(value) {
            field = value
            state.set("expiryDateVPU", value)
        }
    var policyAmount = state.get<String>("policyAmountVPU") ?: vehiclePolicyArgs?.policyAmount ?: ""
        set(value) {
            field = value
            state.set("policyAmountVPU", value)
        }
    var policyType = state.get<String>("policyTypeVPU") ?: vehiclePolicyArgs?.policyType ?: ""
        set(value) {
            field = value
            state.set("policyTypeVPU", value)
        }
    var updatePolicyButton = state.get<Boolean>("updateButtonVPU") ?: false
        set(value) {
            field = value
            state.set("updateButtonVPU", value)
        }

    fun updatePolicy(vehiclePolicy: VehiclePolicy) = viewModelScope.launch {
        vehiclePolicyRepository.updateVehiclePolicy(vehiclePolicy)
    }

    fun onUpdateClick() = viewModelScope.launch {

        if (companyName.isEmpty() || companyLogo == 0 || policyNumber.isEmpty() || vehicleNumber.isEmpty() || vehicleName.isEmpty() || policyAmount.isEmpty()) {
            eventChannel.send(Response.NotValid(isEmpty = true))
        }else if(purchaseDate > expiryDate) {
            eventChannel.send(Response.NotValid(higherPurchaseDate = true))
        }

        val vehiclePolicy = VehiclePolicy(companyName, companyLogo, policyNumber, vehicleNumber, vehicleName, purchaseDate, expiryDate, policyAmount, policyType, id)
        updatePolicy(vehiclePolicy)
        eventChannel.send(Response.Valid)
    }
}