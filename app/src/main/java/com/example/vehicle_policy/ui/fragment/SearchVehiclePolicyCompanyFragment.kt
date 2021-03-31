package com.example.vehicle_policy.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.databinding.FragmentSearchVehiclePolicyCompanyBinding
import com.example.vehicle_policy.ui.adapters.SearchVehiclePolicyCompanysAdapter
import com.example.vehicle_policy.ui.viewmodel.SearchVehiclePolicyCompanysViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchVehiclePolicyCompanyFragment constructor(
    var viewModel: SearchVehiclePolicyCompanysViewModel? = null
) : Fragment(R.layout.fragment_search_vehicle_policy_company),
    SearchVehiclePolicyCompanysAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSearchVehiclePolicyCompanyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(this).get(SearchVehiclePolicyCompanysViewModel::class.java)
        binding = FragmentSearchVehiclePolicyCompanyBinding.bind(view)
        val vehiclePolicyAdapter = SearchVehiclePolicyCompanysAdapter(this)

        binding.apply {
            recyclerViewVpSearch.apply {
                adapter = vehiclePolicyAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ivBackVpSearch.setOnClickListener {
                findNavController().popBackStack()
            }
            etSearchCompanyVpSearch.addTextChangedListener {
                viewModel?.searchQuery?.value = it.toString()
            }
        }

        viewModel?.vehiclePolicyCompanys?.observe(viewLifecycleOwner) {
            vehiclePolicyAdapter.submitList(it)
        }
    }

    override fun onItemClick(vehiclePolicyCompanys: VehiclePolicyCompanys) {
        val action = SearchVehiclePolicyCompanyFragmentDirections.actionSearchVehiclePolicyCompanyFragmentToAddVehiclePolicyFragment(vehiclePolicyCompanys, true)
        findNavController().navigate(action)
    }
}