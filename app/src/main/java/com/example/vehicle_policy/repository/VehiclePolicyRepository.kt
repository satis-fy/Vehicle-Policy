package com.example.vehicle_policy.repository

import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import kotlinx.coroutines.flow.Flow

interface VehiclePolicyRepository {

    fun getAllPolicy(): Flow<List<VehiclePolicy>>

    fun getPolicyByCompany(companyName: String): Flow<List<VehiclePolicy>>

    suspend fun addVehiclePolicy(vehiclePolicy: VehiclePolicy)

    suspend fun updateVehiclePolicy(vehiclePolicy: VehiclePolicy)

    suspend fun deleteVehiclePolicy(vehiclePolicy: VehiclePolicy)

    fun getAllVehiclePolicyCompany(): Flow<List<VehiclePolicyCompanys>>

    suspend fun addVehiclePolicyCompanys(vehiclePolicyCompanys: VehiclePolicyCompanys)

    fun getVehiclePolicyCompanys(searchQuery: String): Flow<List<VehiclePolicyCompanys>>
}