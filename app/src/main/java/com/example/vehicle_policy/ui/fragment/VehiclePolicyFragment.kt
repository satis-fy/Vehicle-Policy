package com.example.vehicle_policy.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.databinding.FragmentVehiclePolicyBinding
import com.example.vehicle_policy.ui.adapters.VehiclePolicyAdapter
import com.example.vehicle_policy.ui.viewmodel.VehiclePolicyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehiclePolicyFragment constructor(
    var viewModel: VehiclePolicyViewModel? = null
) : Fragment(R.layout.fragment_vehicle_policy),
    VehiclePolicyAdapter.OnItemClickListener {

    private lateinit var binding: FragmentVehiclePolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(VehiclePolicyViewModel::class.java)
        binding = FragmentVehiclePolicyBinding.bind(view)
        val vehiclePolicyAdapter = VehiclePolicyAdapter(this)

        binding.apply {
            recyclerViewVp.apply {
                adapter = vehiclePolicyAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ivBackVp.setOnClickListener {
                findNavController().popBackStack()
            }
            ivSearchCompanyVp.setOnClickListener {
                findNavController().navigate(VehiclePolicyFragmentDirections.actionVehiclePolicyFragmentToSearchVehiclePolicyCompanyFragment())
            }
        }

        viewModel?.getAllVehiclePolicyCompany?.observe(viewLifecycleOwner) {
            vehiclePolicyAdapter.submitList(it)
        }
    }

    override fun onItemClick(vehicleCompany: VehiclePolicyCompanys) {
        val action = VehiclePolicyFragmentDirections.actionVehiclePolicyFragmentToListVehiclePolicyFragment(vehicleCompany)
        findNavController().navigate(action)
    }
}