package com.example.vehicle_policy.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import kotlinx.coroutines.flow.Flow

class FakeAndroidTestVehiclePolicyRepository : VehiclePolicyRepository {

    private val vehiclePolicys = mutableListOf<VehiclePolicy>()
    private val observableVehiclePolicy = MutableLiveData<List<VehiclePolicy>>(vehiclePolicys)

    private val vehiclePolicyCompanys = mutableListOf<VehiclePolicyCompanys>()
    private val observableVehiclePolicyCompanys =
        MutableLiveData<List<VehiclePolicyCompanys>>(vehiclePolicyCompanys)

    private fun refreshLiveData() {
        observableVehiclePolicy.postValue(vehiclePolicys)
    }

    private fun refreshCompanyLiveData() {
        observableVehiclePolicyCompanys.postValue(vehiclePolicyCompanys)
    }

    override fun getAllPolicy(): Flow<List<VehiclePolicy>> {
        return observableVehiclePolicy.asFlow()
    }

    override fun getPolicyByCompany(companyName: String): Flow<List<VehiclePolicy>> {

        val list = Transformations.map(observableVehiclePolicy) {
            it.filter { vehiclePolicy ->
                vehiclePolicy.companyName.contains(companyName)
            }
        }
        return list.asFlow()
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
        refreshLiveData()
    }

    override fun getAllVehiclePolicyCompany(): Flow<List<VehiclePolicyCompanys>> {
        return observableVehiclePolicyCompanys.asFlow()
    }

    override suspend fun addVehiclePolicyCompanys(vehiclePolicyCompanys: VehiclePolicyCompanys) {
        this.vehiclePolicyCompanys.add(vehiclePolicyCompanys)
        refreshCompanyLiveData()
    }

    override fun getVehiclePolicyCompanys(searchQuery: String): Flow<List<VehiclePolicyCompanys>> {

        val list = Transformations.map(observableVehiclePolicyCompanys) {
            it.filter { vehicleCompany ->
                vehicleCompany.companyName.contains(searchQuery)
            }
        }
        return list.asFlow()
    }
}