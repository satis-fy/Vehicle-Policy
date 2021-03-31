package com.example.vehicle_policy.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.SavedStateHandle
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.repository.FakeAndroidTestVehiclePolicyRepository
import com.example.vehicle_policy.ui.viewmodel.ListVehiclePolicyViewModel
import com.example.vehicle_policy.ui.viewmodel.SearchVehiclePolicyCompanysViewModel
import com.example.vehicle_policy.ui.viewmodel.ShowVehiclePolicyViewModel
import com.example.vehicle_policy.ui.viewmodel.VehiclePolicyViewModel
import javax.inject.Inject

class TestVehiclePolicyFactory @Inject constructor(): FragmentFactory() {

    val  fakeAndroidTestRepository = FakeAndroidTestVehiclePolicyRepository()
    val state = SavedStateHandle()

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {

            VehiclePolicyFragment::class.java.name -> VehiclePolicyFragment(VehiclePolicyViewModel(fakeAndroidTestRepository))

            ListVehiclePolicyFragment::class.java.name -> {
                val vehiclePolicyCompany = VehiclePolicyCompanys("LIC",1,id = 1)
                state.set("singleCompanyNameLogo",vehiclePolicyCompany)
                ListVehiclePolicyFragment(ListVehiclePolicyViewModel(fakeAndroidTestRepository,state))
            }

            SearchVehiclePolicyCompanyFragment::class.java.name -> SearchVehiclePolicyCompanyFragment(SearchVehiclePolicyCompanysViewModel(fakeAndroidTestRepository))

            ShowVehiclePolicyFragment::class.java.name -> {
                val vehiclePolicy = VehiclePolicy("LIC", 1, "123", "GJ", "Maruti", 1616934268028, 1616934268028, "456", "Full", id = 1)
                state.set("showVehiclePolicy", vehiclePolicy)
                ShowVehiclePolicyFragment(ShowVehiclePolicyViewModel(state))
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}