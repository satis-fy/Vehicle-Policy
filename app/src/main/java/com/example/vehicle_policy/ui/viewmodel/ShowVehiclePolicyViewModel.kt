package com.example.vehicle_policy.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.vehicle_policy.data.VehiclePolicy

class ShowVehiclePolicyViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val vehiclePolicyArgs = state.get<VehiclePolicy>("showVehiclePolicy")

    val companyName = vehiclePolicyArgs?.companyName ?: "Copied"
    val policyNumber = vehiclePolicyArgs?.policyNumber
    val vehicleNumber = vehiclePolicyArgs?.vehicleNumber
    val vehicleName = vehiclePolicyArgs?.vehicleName
    val purchaseDate = vehiclePolicyArgs?.purchaseDate ?: 1L
    val expiryDate = vehiclePolicyArgs?.expiryDate ?: 1L
    val policyAmount = vehiclePolicyArgs?.policyAmount
    val policyType = vehiclePolicyArgs?.policyType ?: ""

    fun copyClipBoard(): StringBuilder {
        val copyText = StringBuilder()
        copyText.append("Company Name : ${companyName} \n" +
                    "Policy Number : ${policyNumber} \n" +
                    "Vehicle Number : ${vehicleNumber} \n" +
                    "Vehicle Name : ${vehicleName} \n" +
                    "Purchase Date : ${purchaseDate} \n" +
                    "Expiry Date : ${expiryDate} \n" +
                    "Policy Amount : ${policyAmount} \n"
        )
        if (policyType.isNotEmpty()) {
            copyText.append("Policy Type : ${policyType} \n")
        }
        return copyText
    }
}