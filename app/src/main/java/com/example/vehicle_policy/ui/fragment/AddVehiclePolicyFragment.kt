package com.example.vehicle_policy.ui.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.vehicle_policy.R
import com.example.vehicle_policy.databinding.FragmentAddVehiclePolicyBinding
import com.example.vehicle_policy.ui.viewmodel.AddVehiclePolicyViewModel
import com.example.vehicle_policy.util.Response
import com.example.vehicle_policy.util.exhaustive
import com.example.vehicle_policy.util.mySnackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddVehiclePolicyFragment :
    Fragment(R.layout.fragment_add_vehicle_policy) {

    //private val viewModel by viewModels<AddVehiclePolicyViewModel>()
    lateinit var viewModel: AddVehiclePolicyViewModel
    private lateinit var binding: FragmentAddVehiclePolicyBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AddVehiclePolicyViewModel::class.java)
        binding = FragmentAddVehiclePolicyBinding.bind(view)

        binding.apply {

            ivBackVpi.setOnClickListener {
                findNavController().popBackStack()
            }

            etCompanyNameVpi.setText(viewModel.companyName)
            etCompanyNameVpi.addTextChangedListener {
                viewModel.companyName = it.toString()
                checkSavePolicyButton()
            }
            etPolicyNumberVpi.setText(viewModel.policyNumber)
            etPolicyNumberVpi.addTextChangedListener {
                viewModel.policyNumber = it.toString()
                checkSavePolicyButton()
            }
            etVehicleNumberVpi.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
            etVehicleNumberVpi.setText(viewModel.vehicleNumber)
            etVehicleNumberVpi.addTextChangedListener {
                viewModel.vehicleNumber = it.toString()
                checkSavePolicyButton()
            }
            etVehicleNameVpi.setText(viewModel.vehicleName)
            etVehicleNameVpi.addTextChangedListener {
                viewModel.vehicleName = it.toString()
                checkSavePolicyButton()
            }
            etPurchaseDateVpi.setOnClickListener {
                datePickerPurchase()
            }
            etExpiryDateVpi.setOnClickListener {
                datePickerExpiry()
            }
            etPolicyTypeVpi.setText(viewModel.policyType)
            etPolicyTypeVpi.addTextChangedListener {
                viewModel.policyType = it.toString()
                checkSavePolicyButton()
            }
            etPolicyAmountVpi.setText(viewModel.policyAmount)
            etPolicyAmountVpi.addTextChangedListener {
                viewModel.policyAmount = it.toString()
                checkSavePolicyButton()
            }
            btAddPolicyVpi.isEnabled = viewModel.addPolicyButton
            btAddPolicyVpi.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect { response ->
                when (response) {
                    is Response.NotValid -> {
                        if (response.higherPurchaseDate)
                            view.mySnackbar(R.string.SnackBar_PolicyHigherPurchaseDate)
                        else
                            view.mySnackbar(R.string.SnackBar_PolicyFieldEmpty)
                    }
                    is Response.Valid -> {
                        view.mySnackbar(R.string.SnackBar_PolicySaved)
                        if (viewModel.fromVehiclePolicyArgs) {
                            val action = AddVehiclePolicyFragmentDirections.actionAddVehiclePolicyFragmentToVehiclePolicyFragment()
                            findNavController().navigate(action)
                        } else {
                            findNavController().popBackStack()
                        }
                    }
                }.exhaustive
            }
        }
    }

    private fun checkSavePolicyButton() {
        binding.apply {
            btAddPolicyVpi.isEnabled = etCompanyNameVpi.text.toString().isNotEmpty() &&
                    etPolicyNumberVpi.text.toString().isNotEmpty() &&
                    etVehicleNumberVpi.text.toString().isNotEmpty() &&
                    etVehicleNameVpi.text.toString().isNotEmpty() &&
                    etPurchaseDateVpi.text.toString().isNotEmpty() &&
                    etExpiryDateVpi.text.toString().isNotEmpty() &&
                    etPolicyAmountVpi.text.toString().isNotEmpty() &&
                    viewModel.companyLogo != 0 &&
                    viewModel.purchaseDate < viewModel.expiryDate

            viewModel.addPolicyButton = btAddPolicyVpi.isEnabled
        }
    }

    private fun datePickerPurchase() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year2, monthOfYear, dayOfMonth ->
            c.set(year2, monthOfYear, dayOfMonth)
            binding.etPurchaseDateVpi.setText("${dayOfMonth}/${monthOfYear + 1}/${year2}")
            viewModel.purchaseDate = c.timeInMillis
            checkSavePolicyButton()
        }, year, month, day)
        dpd.datePicker.maxDate = System.currentTimeMillis()
        dpd.show()
    }

    private fun datePickerExpiry() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { view, year2, monthOfYear, dayOfMonth ->
            c.set(year2, monthOfYear, dayOfMonth)
            binding.etExpiryDateVpi.setText("${dayOfMonth}/${monthOfYear + 1}/${year2}")
            viewModel.expiryDate = c.timeInMillis
            checkSavePolicyButton()
        }, year, month, day)
        dpd.datePicker.minDate = viewModel.purchaseDate
        dpd.show()
    }
}