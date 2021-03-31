package com.example.vehicle_policy.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.data.VehiclePolicyDao
import com.example.vehicle_policy.data.VehiclePolicyDatabase
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class VehiclePolicyRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("androidTest_db")
    lateinit var database: VehiclePolicyDatabase
    private lateinit var dao: VehiclePolicyDao
    private lateinit var repository: DefaultVehiclePolicyRepository

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.vehiclePolicyDao()
        repository = DefaultVehiclePolicyRepository(dao)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun addVehiclePolicy() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)

        repository.addVehiclePolicy(vehiclePolicy)

        val getVehiclePolicy = repository.getAllPolicy().first()
        assertThat(getVehiclePolicy).contains(vehiclePolicy)
    }

    @Test
    fun deleteVehiclePolicy() = runBlockingTest {
        val vehiclePolicy = VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        repository.addVehiclePolicy(vehiclePolicy)
        repository.deleteVehiclePolicy(vehiclePolicy)

        val getVehiclePolicy = repository.getAllPolicy().first()
        assertThat(getVehiclePolicy).doesNotContain(vehiclePolicy)
    }

    @Test
    fun updateVehiclePolicy() = runBlockingTest {
        val vehiclePolicy = VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        repository.addVehiclePolicy(vehiclePolicy)

        val updatedVehiclePolicy = VehiclePolicy("MAX", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        repository.updateVehiclePolicy(updatedVehiclePolicy)

        val getVehiclePolicy = repository.getAllPolicy().first()
        assertThat(getVehiclePolicy).contains(updatedVehiclePolicy)
    }

    @Test
    fun addVehiclePolicyCompany() = runBlockingTest {
        val vehiclePolicy = VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        repository.addVehiclePolicy(vehiclePolicy)

        val getVehiclePolicyCompany = repository.getAllVehiclePolicyCompany().first()
        assertThat(getVehiclePolicyCompany).contains(VehiclePolicyCompanys("LIC",0,id = 1))
    }

    @Test
    fun getVehiclePolicySearchCompany() = runBlockingTest {
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",0,id = 1)
        repository.addVehiclePolicyCompanys(vehiclePolicyCompany)

        val getVehiclePolicyCompany = repository.getVehiclePolicyCompanys("LIC").first()
        assertThat(getVehiclePolicyCompany).contains(vehiclePolicyCompany)
    }

    @Test
    fun getVehiclePolicyByCompanyName() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        repository.addVehiclePolicy(vehiclePolicy)

        val getVehiclePolicy = repository.getPolicyByCompany("LIC").first()
        assertThat(getVehiclePolicy).contains(vehiclePolicy)
    }
}