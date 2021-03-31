package com.example.vehicle_policy.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import kotlinx.coroutines.flow.Flow

class FakeVehiclePolicyRepository: VehiclePolicyRepository {

    private val vehiclePolicys = mutableListOf<VehiclePolicy>()
    private val observableVehiclePolicy = MutableLiveData<List<VehiclePolicy>>(vehiclePolicys)

    private val vehiclePolicyCompany = mutableListOf<VehiclePolicyCompanys>()
    private val observableVehiclePolicyCompany = MutableLiveData<List<VehiclePolicyCompanys>>(vehiclePolicyCompany)

    private fun refreshLiveData(){
        observableVehiclePolicy.postValue(vehiclePolicys)
    }

    private fun refreshCompanyLiveData(){
        observableVehiclePolicyCompany.postValue(vehiclePolicyCompany)
    }

    override fun getAllPolicy(): Flow<List<VehiclePolicy>> {
        return observableVehiclePolicy.asFlow()
    }

    override fun getPolicyByCompany(companyName: String): Flow<List<VehiclePolicy>> {
        return observableVehiclePolicy.asFlow()
    }

    override suspend fun addVehiclePolicy(vehiclePolicy: VehiclePolicy) {
        vehiclePolicys.add(vehiclePolicy)
        refreshLiveData()
    }

    override suspend fun updateVehiclePolicy(vehiclePolicy: VehiclePolicy) {
        vehiclePolicys[0] = vehiclePolicy
        refreshLiveData()
    }

    override suspend fun deleteVehiclePolicy(vehiclePolicy: VehiclePolicy) {
        vehiclePolicys.remove(vehiclePolicy)
        refreshCompanyLiveData()
    }

    override fun getAllVehiclePolicyCompany(): Flow<List<VehiclePolicyCompanys>> {
        return observableVehiclePolicyCompany.asFlow()
    }

    override suspend fun addVehiclePolicyCompanys(vehiclePolicyCompanys: VehiclePolicyCompanys) {
        this.vehiclePolicyCompany.add(vehiclePolicyCompanys)
        refreshCompanyLiveData()
    }

    override fun getVehiclePolicyCompanys(searchQuery: String): Flow<List<VehiclePolicyCompanys>> {
        return observableVehiclePolicyCompany.asFlow()
    }
}