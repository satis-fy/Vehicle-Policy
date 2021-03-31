package com.example.vehicle_policy.repository

import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.data.VehiclePolicyDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultVehiclePolicyRepository @Inject constructor(private val vehiclePolicyDao: VehiclePolicyDao) :
    VehiclePolicyRepository {

    override fun getAllPolicy(): Flow<List<VehiclePolicy>> {
        return vehiclePolicyDao.getVehiclePolicy() 
    }

    override fun getPolicyByCompany(companyName: String): Flow<List<VehiclePolicy>> {
        return vehiclePolicyDao.getVehiclePolicyByCompanyName(companyName)
    }

    override suspend fun addVehiclePolicy(vehiclePolicy: VehiclePolicy) =
        vehiclePolicyDao.insert(vehiclePolicy) 

    override suspend fun updateVehiclePolicy(vehiclePolicy: VehiclePolicy) =
        vehiclePolicyDao.update(vehiclePolicy) 

    override suspend fun deleteVehiclePolicy(vehiclePolicy: VehiclePolicy) =
        vehiclePolicyDao.delete(vehiclePolicy) 

    override fun getAllVehiclePolicyCompany(): Flow<List<VehiclePolicyCompanys>> {
        return vehiclePolicyDao.getAllVehiclePolicyCompany() 
    }

    override suspend fun addVehiclePolicyCompanys(vehiclePolicyCompanys: VehiclePolicyCompanys) =
        vehiclePolicyDao.insertCompany(vehiclePolicyCompanys) 

    override fun getVehiclePolicyCompanys(searchQuery: String): Flow<List<VehiclePolicyCompanys>> {
        return vehiclePolicyDao.getVehiclePolicyCompanys(searchQuery) 
    }
}