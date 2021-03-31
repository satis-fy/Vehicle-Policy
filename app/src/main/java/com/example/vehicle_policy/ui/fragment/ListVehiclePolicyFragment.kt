package com.example.vehicle_policy.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicle_policy.R
import com.example.vehicle_policy.data.VehiclePolicy
import com.example.vehicle_policy.data.VehiclePolicyCompanys
import com.example.vehicle_policy.databinding.FragmentListVehiclePolicyBinding
import com.example.vehicle_policy.ui.adapters.ListVehiclePolicyAdapter
import com.example.vehicle_policy.ui.viewmodel.ListVehiclePolicyViewModel
import com.example.vehicle_policy.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ListVehiclePolicyFragment constructor(
    var viewModel: ListVehiclePolicyViewModel? = null
) : Fragment(R.layout.fragment_list_vehicle_policy), ListVehiclePolicyAdapter.OnItemClickListener {

    private lateinit var binding: FragmentListVehiclePolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = viewModel ?: ViewModelProvider(this).get(ListVehiclePolicyViewModel::class.java)
        binding = FragmentListVehiclePolicyBinding.bind(view)
        val listVehiclePolicyAdapter = ListVehiclePolicyAdapter(this)

        binding.apply {
            tvTitleCompanyNameVpl.text = viewModel?.companyName

            recyclerViewVpl.apply {
                adapter = listVehiclePolicyAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            ivBackVpl.setOnClickListener {
                findNavController().popBackStack()
            }
            ivAddPolicyIconVpl.setOnClickListener {
                findNavController().navigate(
                    ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToAddVehiclePolicyFragment(VehiclePolicyCompanys(viewModel!!.companyName, viewModel!!.companyLogo), false)
                )
            }
        }

        viewModel!!.vehiclePolicyByCompanyName.observe(viewLifecycleOwner) {
            listVehiclePolicyAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel!!.event.collect { event ->
                when (event) {
                    is ListVehiclePolicyViewModel.Event.ShowDeletePolicyDialog -> {
                        Snackbar.make(requireView(), R.string.SnackBar_PolicyDelete, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(vehiclePolicy: VehiclePolicy) {
        val action = ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToShowVehiclePolicyFragment(vehiclePolicy)
        findNavController().navigate(action)
    }

    override fun onOptionIconClick(vehiclePolicy: VehiclePolicy, view: View) {
        val popMenu = PopupMenu(requireContext(), view)
        popMenu.menuInflater.inflate(R.menu.delete_edit_options, popMenu.menu)
        popMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_edit_vpl -> {
                    updatePolicy(vehiclePolicy)
                    true
                }
                R.id.menu_delete_vpl -> {
                    deletePolicy(vehiclePolicy)
                    true
                }
                else -> false
            }
        }
        popMenu.show()
    }

    private fun updatePolicy(vehiclePolicy: VehiclePolicy) {
        val action = ListVehiclePolicyFragmentDirections.actionListVehiclePolicyFragmentToUpdateVehiclePolicyFragment(vehiclePolicy)
        findNavController().navigate(action)
    }

    private fun deletePolicy(vehiclePolicy: VehiclePolicy) {
        viewModel?.onPolicyDelete(vehiclePolicy)
    }
}