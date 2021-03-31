package com.example.vehicle_policy.ui

import com.example.vehicle_policy.ui.viewmodel.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AddVehiclePolicyViewModelTest::class,
    ListVehiclePolicyViewModelTest::class,
    SearchVehiclePolicyCompanysViewModelTest::class,
    ShowVehiclePolicyViewModelTest::class,
    UpdateVehiclePolicyViewModelTest::class,
    VehiclePolicyViewModelTest::class
)
class ViewModelTestSuite