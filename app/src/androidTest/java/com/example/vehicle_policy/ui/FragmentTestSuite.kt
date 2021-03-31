package com.example.vehicle_policy.ui

import com.example.vehicle_policy.ui.fragment.*
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    AddVehiclePolicyFragmentTest::class,
    ListVehiclePolicyFragmentTest::class,
    SearchVehiclePolicyCompanyFragmentTest::class,
    ShowVehiclePolicyFragmentTest::class,
    UpdateVehiclePolicyFragmentTest::class,
    VehiclePolicyFragmentTest::class
)
class FragmentTestSuite