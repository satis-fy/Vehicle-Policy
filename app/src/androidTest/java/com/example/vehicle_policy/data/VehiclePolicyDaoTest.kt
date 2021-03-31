package com.example.vehicle_policy.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
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
class VehiclePolicyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("androidTest_db")
    lateinit var database: VehiclePolicyDatabase
    private lateinit var dao: VehiclePolicyDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.vehiclePolicyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertVehiclePolicy() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.insert(vehiclePolicy)

        // first() return first element of Flow<> of List<VehiclePolicy>
        val getVehiclePolicy = dao.getVehiclePolicy().first()
        assertThat(getVehiclePolicy).contains(vehiclePolicy)
    }

    @Test
    fun deleteVehiclePolicy() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.insert(vehiclePolicy)
        dao.delete(vehiclePolicy)

        val getVehiclePolicy = dao.getVehiclePolicy().first()
        assertThat(getVehiclePolicy).doesNotContain(vehiclePolicy)
    }

    @Test
    fun updateVehiclePolicy() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.insert(vehiclePolicy)

        val updatedVehiclePolicy =
            VehiclePolicy("MAX", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.update(updatedVehiclePolicy)

        val getVehiclePolicy = dao.getVehiclePolicy().first()
        assertThat(getVehiclePolicy).contains(updatedVehiclePolicy)
    }

    @Test
    fun getVehiclePolicyByCompanyName() = runBlockingTest {
        val vehiclePolicy =
            VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.insert(vehiclePolicy)

        val getVehiclePolicy = dao.getVehiclePolicyByCompanyName("LIC").first()
        assertThat(getVehiclePolicy).contains(vehiclePolicy)
    }

    @Test
    fun getAllPolicyCompany() = runBlockingTest {
        val vehiclePolicy = VehiclePolicy("LIC", 0,"123", "GJ", "Maruti", 123L, 456L, "12345", "Full", id = 1)
        dao.insert(vehiclePolicy)

        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",0,id = 1)
        val getVehiclePolicyCompany = dao.getAllVehiclePolicyCompany().first()
        assertThat(getVehiclePolicyCompany).contains(vehiclePolicyCompany)
    }

    @Test
    fun insertCompany() = runBlockingTest {
        val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",0,id = 1)
        dao.insertCompany(vehiclePolicyCompany)

        val getVehiclePolicyCompany = dao.getVehiclePolicyCompanys("LIC").first()
        assertThat(getVehiclePolicyCompany).contains(vehiclePolicyCompany)
    }
}