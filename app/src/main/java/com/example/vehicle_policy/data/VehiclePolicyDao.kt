package com.example.vehicle_policy.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclePolicyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehiclePolicy: VehiclePolicy)

    @Query("SELECT * FROM vehicle_policy")
    fun getVehiclePolicy(): Flow<List<VehiclePolicy>>

    @Update
    suspend fun update(vehiclePolicy: VehiclePolicy)

    @Delete
    suspend fun delete(vehiclePolicy: VehiclePolicy)

    @Query("SELECT * FROM vehicle_policy WHERE companyName= :companyName")
    fun getVehiclePolicyByCompanyName(companyName: String): Flow<List<VehiclePolicy>>

    @Query("SELECT DISTINCT companyName,companyLogo,id FROM vehicle_policy GROUP BY companyName ORDER BY id DESC")
    fun getAllVehiclePolicyCompany(): Flow<List<VehiclePolicyCompanys>>

    @Insert
    suspend fun insertCompany(vehiclePolicyCompanys: VehiclePolicyCompanys)

    @Query("SELECT * FROM vehiclePolicy_companys WHERE companyName LIKE '%' || :searchQuery || '%'  ORDER BY companyName ASC")
    fun getVehiclePolicyCompanys(searchQuery: String): Flow<List<VehiclePolicyCompanys>>
}
